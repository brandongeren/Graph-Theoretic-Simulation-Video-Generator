package gephiFirstTask;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;


public class SimpleImageExporter {
	public static void main(String[] args) throws IOException {
		
		GraphMaker gm = new GraphMaker();
		gm.importGraph("time=100.gephi_sex.gexf");
		gm.filterByDegree(1);
		gm.rankColorByDegree(new Color(0xFFFF00), new Color(0xFF0000));
		gm.rankSizeByCentrality(3, 13);
		ConvergenceCriteria cc = new AverageDeltaConvergence(gm.getGraphModel().factory());
		gm.layoutUntilConverged(1f, cc);
		gm.initPreviewModel(false, new Color(0x0000FF), 0.1f);
		gm.exportPNG("sexdata", 10000, 10000);
		
        

       
//     	// set up appearance classes
//         AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
//         AppearanceModel appearanceModel = appearanceController.getModel();
//         
//         // Get Centrality
//         GraphDistance distance = new GraphDistance();
//         distance.setDirected(true);
//         distance.execute(graphModel);

//         // Rank size by centrality
//         Column centralityColumn = graphModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
//         Function centralityRanking = appearanceModel.getNodeFunction(graph, centralityColumn, RankingNodeSizeTransformer.class);
//         RankingNodeSizeTransformer centralityTransformer = (RankingNodeSizeTransformer) centralityRanking.getTransformer();
//         centralityTransformer.setMinSize(3);
//         centralityTransformer.setMaxSize(13);
//         appearanceController.transform(centralityRanking);
    	
//         // ego filters show nodes closely connected to whatever is put into the filter
//         EgoFilter egoFilter = new EgoFilter();
//         egoFilter.setPattern("blogsforbush.com");
//         egoFilter.setDepth(1);
//         Query queryEgo = filterController.createQuery(egoFilter);
//         GraphView viewEgo = filterController.filter(queryEgo);
//         graphModel.setVisibleView(viewEgo);
        
        
	}
	
}
