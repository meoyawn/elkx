package elkx

import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider
import org.eclipse.elk.core.LayoutConfigurator
import org.eclipse.elk.core.RecursiveGraphLayoutEngine
import org.eclipse.elk.core.data.LayoutMetaDataService
import org.eclipse.elk.core.options.CoreOptions
import org.eclipse.elk.core.util.BasicProgressMonitor
import org.eclipse.elk.core.util.ElkUtil
import org.eclipse.elk.graph.json.JsonExporter
import org.eclipse.elk.graph.json.JsonImporter
import com.google.gson.JsonObject as GsonJsonObject

private val imp = JsonImporter()
private val eng = RecursiveGraphLayoutEngine()
private val exp = JsonExporter()

private val svc = LayoutMetaDataService.getInstance().apply {
    registerLayoutMetaDataProviders(CoreOptions())
    registerLayoutMetaDataProviders(LayeredMetaDataProvider())
}

fun layout(obj: GsonJsonObject) {
    val root = imp.transform(obj)
    ElkUtil.applyVisitors(root, LayoutConfigurator())
    eng.layout(root, BasicProgressMonitor())
    imp.transferLayout(root)
}
