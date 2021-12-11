package elkx

import com.google.gson.JsonObject
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider
import org.eclipse.elk.core.LayoutConfigurator
import org.eclipse.elk.core.RecursiveGraphLayoutEngine
import org.eclipse.elk.core.data.LayoutMetaDataService
import org.eclipse.elk.core.options.CoreOptions
import org.eclipse.elk.core.util.BasicProgressMonitor
import org.eclipse.elk.core.util.ElkUtil
import org.eclipse.elk.graph.json.JsonExporter
import org.eclipse.elk.graph.json.JsonImporter

private val imp = JsonImporter()
private val eng = RecursiveGraphLayoutEngine()
private val exp = JsonExporter()

private val svc = LayoutMetaDataService.getInstance().apply {
    registerLayoutMetaDataProviders(CoreOptions())
    registerLayoutMetaDataProviders(LayeredMetaDataProvider())
}

private val bpm = BasicProgressMonitor()

fun layout(obj: JsonObject) {
    val root = imp.transform(obj)
    ElkUtil.applyVisitors(root, LayoutConfigurator())
    eng.layout(root, bpm)
    imp.transferLayout(root)
}
