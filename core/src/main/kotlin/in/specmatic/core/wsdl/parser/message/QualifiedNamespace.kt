package `in`.specmatic.core.wsdl.parser.message

import `in`.specmatic.core.value.XMLNode
import `in`.specmatic.core.value.namespacePrefix
import `in`.specmatic.core.value.withoutNamespacePrefix
import `in`.specmatic.core.wsdl.parser.WSDL

class QualifiedNamespace(val element: XMLNode, private val wsdlTypeReference: String, val wsdl: WSDL) :
    NamespaceQualification {
    override val namespacePrefix: List<String>
        get() {
            return if(wsdlTypeReference.namespacePrefix().isNotBlank())
                listOf(wsdl.mapToNamespacePrefixInDefinitions(wsdlTypeReference.namespacePrefix(), element))
            else
                emptyList() // TODO Open caveat: we are not providing a prefix here, if the type reference didn't contain it, because it was defined inline, and did not have to be looked up. But if it is provided in a qualified schema, does it need a namespace?
        }

    override val nodeName: String
        get() {
            val nodeName = element.getAttributeValue("name")
            return "${wsdlTypeReference.namespacePrefix()}:${nodeName.withoutNamespacePrefix()}"
        }
}