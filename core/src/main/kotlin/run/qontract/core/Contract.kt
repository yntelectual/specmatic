package run.qontract.core

import run.qontract.core.utilities.BrokerClient.getContract
import run.qontract.core.utilities.contractFilePath
import run.qontract.core.utilities.readFile
import run.qontract.fake.ContractFake
import run.qontract.test.ContractTestException
import run.qontract.test.HttpClient
import java.io.IOException

data class Contract constructor(val contractGherkin: String, val majorVersion: Int = 0, val minorVersion: Int = 0) {
    fun startFake(port: Int) = ContractFake(contractGherkin, "localhost", port)

    fun test(endPoint: String) {
        val contractBehaviour = ContractBehaviour(contractGherkin)
        val executionInfo = contractBehaviour.executeTests(HttpClient(endPoint))
        if(executionInfo.hasErrors)
            throw ContractTestException("These contracts are incompatible.\n${executionInfo.generateErrorMessage()}")
    }

    fun test(fake: ContractFake) {
        test(fake.endPoint)
    }

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun forService(name: String?, majorVersion: Int, minorVersion: Int): Contract {
            val contractInfo = getContract(name!!, majorVersion, minorVersion)
            val spec = contractInfo["spec"].toString()
            return Contract(spec, majorVersion, minorVersion)
        }

        @JvmStatic
        fun fromGherkin(contractGherkin: String, majorVersion: Int, minorVersion: Int): Contract {
            return Contract(contractGherkin, majorVersion, minorVersion)
        }

        @JvmStatic
        fun behaviourFromFile(contractFilePath: String) = ContractBehaviour(readFile(contractFilePath))

        @JvmStatic
        fun behaviourFromServiceContractFile() = behaviourFromFile(contractFilePath)
    }
}