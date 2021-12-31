package elkx

import com.google.gson.JsonObject
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider
import org.eclipse.elk.core.LayoutConfigurator
import org.eclipse.elk.core.RecursiveGraphLayoutEngine
import org.eclipse.elk.core.data.LayoutMetaDataService
import org.eclipse.elk.core.data.LayoutOptionData
import org.eclipse.elk.core.options.CoreOptions
import org.eclipse.elk.core.util.BasicProgressMonitor
import org.eclipse.elk.core.util.ElkUtil
import org.eclipse.elk.graph.ElkEdge
import org.eclipse.elk.graph.ElkLabel
import org.eclipse.elk.graph.ElkNode
import org.eclipse.elk.graph.ElkPort
import org.eclipse.elk.graph.json.JsonImporter

private val eng = RecursiveGraphLayoutEngine()

private val SERVICE = LayoutMetaDataService.getInstance().apply {
    registerLayoutMetaDataProviders(CoreOptions())
    registerLayoutMetaDataProviders(LayeredMetaDataProvider())
}

private fun optsToCfg(opts: Map<String, String>): LayoutConfigurator {
    val lc = LayoutConfigurator()
    lc.addFilter(LayoutConfigurator.NO_OVERWRITE)

    for ((k, v) in opts) {
        val option = SERVICE.getOptionDataBySuffix(k) ?: continue
        val value = option.parseValue(v) ?: continue

        if (option.targets.contains(LayoutOptionData.Target.NODES) || option.targets.contains(LayoutOptionData.Target.PARENTS)) {
            lc.configure(ElkNode::class.java).setProperty(option, value)
        }
        if (option.targets.contains(LayoutOptionData.Target.EDGES)) {
            lc.configure(ElkEdge::class.java).setProperty(option, value)
        }
        if (option.targets.contains(LayoutOptionData.Target.PORTS)) {
            lc.configure(ElkPort::class.java).setProperty(option, value)
        }
        if (option.targets.contains(LayoutOptionData.Target.LABELS)) {
            lc.configure(ElkLabel::class.java).setProperty(option, value)
        }
    }

    return lc
}

fun layout(rootJson: JsonObject, opts: Map<String, String>) {
    val imp = JsonImporter()
    val rootNode = imp.transform(rootJson)
    ElkUtil.applyVisitors(rootNode, optsToCfg(opts))
    eng.layout(rootNode, BasicProgressMonitor())
    imp.transferLayout(rootNode)
}
