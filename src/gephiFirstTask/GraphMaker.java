package gephiFirstTask;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.imageio.ImageIO;

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
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Table;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PNGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.jcodec.api.awt.SequenceEncoder;
import org.openide.util.Lookup;


//  class specialized for our data set to reduce the copying and pasting of blocks of code into one-line statements
//  Makes a lot of assumptions about the data, including no self-loops and always directed graph
public class GraphMaker {
	
	/** Todo: 
	 * 
	 * implement an idea of convergence into the initial iterative layout process
	 * implement projection
	 * understand/integrate dynamic graphs
	 * 
	 * **/
	
	private ProjectController pc;
	private Workspace workspace;
	private GraphModel graphModel;
	
	public GraphMaker() {
		this.pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		this.workspace = pc.getCurrentWorkspace();
		this.graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
	}
	
	public GraphModel getGraphModel() {
		return this.graphModel;
	}
	
	public void setGraphModel(GraphModel graphModel) {
		this.graphModel = graphModel;
	}
	
	public void importGraph(String filename) {
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
			
        // Import file       
        Container container;
        try {
        	File file = new File(filename);
            container = importController.importFile(file);
            container.getLoader().setEdgeDefault(EdgeDirectionDefault.DIRECTED);   // Force DIRECTED
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        
        // Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);
	}
	
	// create a PNG file of the graph, using the given height and width, put it at the location filename
	public void exportPNG(String filename, int height, int width) throws IOException {
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        PNGExporter pngEx = (PNGExporter) ec.getExporter("png");
        
        // modify the dimensions of the PNG
        pngEx.setHeight(height);
        pngEx.setWidth(width);
        // pngEx.setMargin(margin);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ec.exportStream(baos, pngEx);
    	OutputStream outputStream = new FileOutputStream(filename + ".png"); 
    	baos.writeTo(outputStream);
    	outputStream.close();
    	baos.close();
	}
	
	public byte[] exportByteArray(int height, int width) {
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        PNGExporter pngEx = (PNGExporter) ec.getExporter("png");
        
        // modify the dimensions of the PNG
        pngEx.setHeight(height);
        pngEx.setWidth(width);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ec.exportStream(baos, pngEx);
        return baos.toByteArray();
	}
	
	public BufferedImage bufferedImageFromByteArray(byte[] bytes) throws IOException {
		InputStream input = new ByteArrayInputStream(bytes);
		return ImageIO.read(input);
	}
	
	public void exportGraphFile(String filename) throws IOException {
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		ec.exportFile(new File(filename));
	}
	
	// uses jcodec to make a movie file out of a list of images numbered appropriately
	public static void makeMovie(String movieFilename, String movieFormat, String imageDir, String imageFilename, String imageFormat, int numImages) throws IOException {
		String userHome = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");
		
      	int modificationDigits = numDigits(numImages);
      	// use jcodec to encode the images frame by frame
		SequenceEncoder encoder = new SequenceEncoder(new File(userHome + separator + "Movies" + separator + movieFilename + "." + movieFormat));
	    for (int i = 0; i < numImages; i++) {
	    	BufferedImage frame = ImageIO.read(new File(String.format(
	    			imageDir + separator + imageFilename + "%0" + modificationDigits + "d." + imageFormat, i)));
	    	encoder.encodeImage(frame);;
	    }
	    encoder.finish();
	}
	
	public void encodeFrame(SequenceEncoder encoder) throws IOException {
		BufferedImage currentFrame = this.bufferedImageFromByteArray(this.exportByteArray(1080, 1920));
		encoder.encodeImage(currentFrame);

	}

	private static int pickRandomIndex(int numThings) {
		return (int) Math.floor(Math.random() * numThings);
	}
	
	public static int numDigits(int num) {
		Integer numObject = new Integer(num);
		String numString = numObject.toString();
		return numString.length();
	}

	public Node pickRandomNode() {
		DirectedGraph graph = this.getGraph();
    	Node[] nodes = graph.getNodes().toArray();
		int nodeCount = nodes.length;
    	int randIndex = pickRandomIndex(nodeCount - 1);
    	return nodes[randIndex];
	}
	
	// make a random new edge for the given node
	// causes an infinite loop if the node has edges to all of the other nodes in the graph
	// may also run arbitrarily wrong if the RNG is not in your favor
	/** potential fix: figure out how to do difference between sets (make the node list / edge list into sets) **/
	public Edge pickRandomEdge(Node node) {
		DirectedGraph graph = this.getGraph();
      	GraphFactory factory = graphModel.factory();
      	
      	Node randNode = this.pickRandomNode();    	
    	Edge newEdge = factory.newEdge(node, randNode);
    	
    	// the first part of the conditional prevents self-directed edges
    	// self-directed edges don't make sense with our data set, so they will not be used
    	while(randNode.equals(node) || graph.contains(newEdge)) {
          	randNode = this.pickRandomNode();    	
        	newEdge = factory.newEdge(node, randNode);
    	}
    	return newEdge;
	}
	
	public void addEdge(Edge edge) {
		DirectedGraph graph = this.getGraph();
		graph.addEdge(edge);
	}
		
	public Edge addEdge(Node source, Node target) {
		DirectedGraph graph = this.getGraph();
		GraphFactory factory = graphModel.factory();
		
		Edge edge = factory.newEdge(source, target);
		graph.addEdge(edge);
		return edge;
	}
	
	public void addNewNode(String id) {
      	GraphFactory factory = graphModel.factory();
      	DirectedGraph graph = this.getGraph();
      	Node newNode = factory.newNode(id);
      	graph.addNode(newNode);
	}
	
	public void addNodes(Collection<Node> nodes) {
		DirectedGraph graph = this.getGraph();
		graph.addAllNodes(nodes);
	}
	
	public void addEdges(Collection<Edge> edges) {
		DirectedGraph graph = this.getGraph();
		graph.addAllEdges(edges);
	}
			

	public void removeRandomEdge(Node node) {
		DirectedGraph graph = this.getGraph();
        if(graph.getNeighbors(node).toArray().length != 0) {
	    	Edge[] edges = graph.getEdges(node).toArray();
	    	int randIndex = pickRandomIndex(edges.length - 1);
	    	graph.removeEdge(edges[randIndex]);
        }
	}
	
	public void rankColorByDegree() {
		this.rankColorByDegree(new Color(0xFF0000), new Color(0xFF0000));
	}
	
	public void rankColorByDegree(Color lowDegree, Color highDegree) {
		DirectedGraph graph = this.getGraph();
      	AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();

        Function degreeRanking = appearanceModel.getNodeFunction(graph, AppearanceModel.GraphFunction.NODE_DEGREE, RankingElementColorTransformer.class);
        RankingElementColorTransformer degreeTransformer = (RankingElementColorTransformer) degreeRanking.getTransformer();
        degreeTransformer.setColors(new Color[]{lowDegree, highDegree});
        degreeTransformer.setColorPositions(new float[]{0f, 1f});
        appearanceController.transform(degreeRanking);
	}
	
	public void rankSizeByCentrality(int minSize, int maxSize) {
		DirectedGraph graph = this.getGraph();
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel);
		AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
		AppearanceModel appearanceModel = appearanceController.getModel();

 	    Column centralityColumn = graphModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
 	    Function centralityRanking = appearanceModel.getNodeFunction(graph, centralityColumn, RankingNodeSizeTransformer.class);
 	    RankingNodeSizeTransformer centralityTransformer = (RankingNodeSizeTransformer) centralityRanking.getTransformer();
 	    centralityTransformer.setMinSize(minSize);
 	    centralityTransformer.setMaxSize(maxSize);
 	    appearanceController.transform(centralityRanking);
	}
	
	public void setEgoFilter(String ego) {
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);

		EgoFilter egoFilter = new EgoFilter();
		egoFilter.setPattern(ego);
		egoFilter.setDepth(1);
		Query queryEgo = filterController.createQuery(egoFilter);
		GraphView viewEgo = filterController.filter(queryEgo);
		graphModel.setVisibleView(viewEgo);
	}
	
	// yifanHu is faster than forceAtlas but doesn't work on graphs layouts
	public void yifanHu(int iterations) {
      	YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
      	layout.setGraphModel(graphModel);
      	layout.resetPropertiesValues();
      	layout.setOptimalDistance(100f);
      	layout.initAlgo();
      	for (int i = 0; i < iterations && layout.canAlgo(); i++) {
      		layout.goAlgo();
      	}
	}
	
	// forceAtlas works on dynamic graphs but is slower than yifanHu
	public void forceAtlas(int iterations) {
      	ForceAtlasLayout layout = new ForceAtlasLayout(null);
      	layout.setGraphModel(graphModel);
      	layout.resetPropertiesValues();
      	layout.initAlgo();
      	for(int i = 0; i < iterations && layout.canAlgo(); i++) {
      		layout.goAlgo();
      	}
	}
	
	// mess with a few visual things. names of variables should be pretty self-explanatory
	public void initPreviewModel(boolean showNodeLabels, Color edgeColor, Float edgeThickness) {
    	PreviewModel pm = Lookup.getDefault().lookup(PreviewController.class).getModel();
        pm.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(edgeColor));
        pm.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(edgeThickness));
        if(showNodeLabels) {
            pm.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, pm.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));    
            pm.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        }
	}
	
	// remove all nodes with degree less than minDegree from the view
	public void filterByDegree(int minDegree) {
		DirectedGraph graph = this.getGraph();
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
        degreeFilter.init(graph);
        degreeFilter.setRange(new Range(minDegree, Integer.MAX_VALUE));     // Remove nodes with degree < minDegree
        Query query = filterController.createQuery(degreeFilter);
        GraphView view = filterController.filter(query);
        graphModel.setVisibleView(view);    // Set the filter result as the visible view
	}
	
	public DirectedGraph getGraph() {
		return this.graphModel.getDirectedGraph();
	}
	
	public Node[] getNodes() {
        DirectedGraph graph = graphModel.getDirectedGraph();
        return graph.getNodes().toArray();
	}
	
	// epsilon represents a metric of how "layed out" the graph should be
	// check if the change between this graph and its next layout is smaller than epsilon
	/** TODO: test this **/
	private boolean converged(float epsilon, ConvergenceCriteria cc) {
		DirectedGraph graph = this.getGraph();
		ArrayList<Node> beforeLayout = copyNodeXY();
		// this.yifanHu(1);
		this.forceAtlas(1);
		graph = this.getGraph();
		ArrayList<Node> afterLayout = new ArrayList<Node>(Arrays.asList(graph.getNodes().toArray()));
		cc.setBeforeLayout(beforeLayout);
		cc.setAfterLayout(afterLayout);
		float change = cc.change();
		// System.out.println(change);
		return (epsilon > change);
	}
	
	// layout until the layout is within epsilon of the previous one
	/** TODO: test this **/
	public void layoutUntilConverged(float epsilon, ConvergenceCriteria cc) {
		boolean converged = false;
		while(!converged) {
			converged = this.converged(epsilon, cc);
		}
	}
	
	public void layoutUntilConverged(float epsilon) {
		GraphFactory gf = this.graphModel.factory();
		AverageDeltaConvergence adc = new AverageDeltaConvergence(gf);
		layoutUntilConverged(epsilon, adc);
	}

	public ArrayList<Node> copyNodeXY() {
      	GraphFactory factory = graphModel.factory();
		DirectedGraph graph = this.getGraph();

      	ArrayList<Node> copyList = new ArrayList<Node>();
      	Node[] originalArray = graph.getNodes().toArray();
      	
      	for(int i = 0; i < originalArray.length; i++) {
    		String label = originalArray[i].getLabel();
    		Node nodeCopy = factory.newNode(label);
    		nodeCopy.setX(originalArray[i].x());
    		nodeCopy.setY(originalArray[i].y());
    		copyList.add(nodeCopy);
      	}
      	return copyList;
	}
	
	public void editNodeProperty(String nodeLabel, String propName, Object propValue) {
		DirectedGraph graph = this.getGraph();
		Node node = graph.getNode(nodeLabel);
		Table nodeTable = node.getTable();
		if (!nodeTable.hasColumn(propName)) {
			nodeTable.addColumn(propName, propValue.getClass());
		}
		node.setAttribute(propName, propValue);
	}
	
	public void addNode(String nodeLabel) {
		DirectedGraph graph = this.getGraph();
		GraphFactory gf = this.getGraphModel().factory();
		Node newNode = gf.newNode(nodeLabel);
		graph.addNode(newNode);
	}
	
	public void removeNode(String nodeLabel) {
		DirectedGraph graph = this.getGraph();
		Node node = graph.getNode(nodeLabel);
		graph.removeNode(node);
	}
	
	public void removeEdge(String nodeLabelSrc, String nodeLabelTgt) {
		DirectedGraph graph = this.getGraph();
		Node sourceNode = graph.getNode(nodeLabelSrc);
		Node targetNode = graph.getNode(nodeLabelTgt);
		Edge edge = graph.getEdge(sourceNode, targetNode);
		graph.removeEdge(edge);
	}
}
