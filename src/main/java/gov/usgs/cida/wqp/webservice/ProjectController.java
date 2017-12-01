package gov.usgs.cida.wqp.webservice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.cida.wqp.dao.intfc.ICountDao;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.delimited.ProjectDelimited;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
import gov.usgs.cida.wqp.parameter.IParameterHandler;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.swagger.SwaggerConfig;
import gov.usgs.cida.wqp.swagger.annotation.FullParameterList;
import gov.usgs.cida.wqp.swagger.model.ProjectCountJson;
import gov.usgs.cida.wqp.util.HttpConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags= {SwaggerConfig.PROJECT_TAG_NAME})
@RestController
@RequestMapping(value=HttpConstants.ENDPOINT_PROJECT,
produces={HttpConstants.MIME_TYPE_TSV,
		HttpConstants.MIME_TYPE_CSV,
		HttpConstants.MIME_TYPE_XLSX,
		HttpConstants.MIME_TYPE_XML})
public class ProjectController extends BaseController {

	public ProjectController(IStreamingDao inStreamingDao, ICountDao inCountDao, IParameterHandler inParameterHandler,
			ILogService inLogService, Integer inMaxResultRows, String inSiteUrlBase,
			ContentNegotiationStrategy inContentStrategy) {
		super(inStreamingDao, inCountDao, inParameterHandler, inLogService, inMaxResultRows, inSiteUrlBase, inContentStrategy);
		// TODO Auto-generated constructor stub
	}
	
	@ApiOperation(value="Return appropriate request headers (including anticipated record counts).")
	@FullParameterList
	@RequestMapping(method=RequestMethod.HEAD)
	public void projectHeadRequest(HttpServletRequest request, HttpServletResponse response) {
		doHeadRequest(request, response);
	}
	
	@ApiOperation(value="Return requested data.", produces="")
	@FullParameterList
	@GetMapping()
	public void projectGetRequest(HttpServletRequest request, HttpServletResponse response) {
		doGetRequest(request, response);
	}

	protected String addCountHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addProjectHeaders(response, counts);
		return HttpConstants.HEADER_TOTAL_PROJECT_COUNT;
	}

	@Override
	protected Profile determineProfile(Map<String, Object> pm) {
		return determineProfile(Profile.PROJECT, pm);
	}

	@Override
	protected Map<String, String> getMapping(Profile profile) {
		return ProjectDelimited.getMapping(profile);
	}

	@Override
	protected IXmlMapping getXmlMapping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IXmlMapping getKmlMapping() {
		return null;
	}
	
}


