package `in`.specmatic.test

import `in`.specmatic.core.log.logger

class TestReport(private val testReportRecords: MutableList<TestResultRecord> = mutableListOf(), private val applicationAPIs: MutableList<API> = mutableListOf(), private val excludedAPIs: List<API> = listOf()) {
    fun addTestReportRecords(testResultRecord: TestResultRecord) {
        testReportRecords.add(testResultRecord)
    }

    fun addAPIs(apis: List<API>) {
        applicationAPIs.addAll(apis)
    }

    fun printReport() {
        if(testReportRecords.isEmpty())
            return

        logger.newLine()
        val apiCoverageReport = ApiCoverageReportGenerator(testReportRecords, applicationAPIs, excludedAPIs).generate()
        logger.log(apiCoverageReport.toLogString())
    }
}