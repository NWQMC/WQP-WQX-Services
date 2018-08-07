package gov.usgs.cida.wqp.swagger;


import java.util.ArrayList;
import java.util.List;

import gov.usgs.cida.wqp.swagger.annotation.ProfileParameterResult;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.base.Optional;

import gov.usgs.cida.wqp.swagger.annotation.FullParameterList;
import gov.usgs.cida.wqp.swagger.annotation.NoQueryParametersList;
import gov.usgs.cida.wqp.swagger.annotation.ProfileParameterActivity;
import gov.usgs.cida.wqp.swagger.annotation.ProfileParameterSummary;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SwaggerParameterBuilder implements OperationBuilderPlugin {

	@Override
	public boolean supports(DocumentationType documentationType) {
		return true;
	}

	@Override
	public void apply(OperationContext context) {
		List<Parameter> parameters = new ArrayList<>();
		Optional<NoQueryParametersList> noQueryParametersList = context.findAnnotation(NoQueryParametersList.class);
		Optional<FullParameterList> fullParameterList = context.findAnnotation(FullParameterList.class);
		Optional<ProfileParameterActivity> profileParameterActivity = context.findAnnotation(ProfileParameterActivity.class);
		Optional<ProfileParameterResult> profileParameterResult = context.findAnnotation(ProfileParameterResult.class);
                Optional<ProfileParameterSummary> profileParameterSummary = context.findAnnotation(ProfileParameterSummary.class);
		Optional<PostMapping> postMapping = context.findAnnotation(PostMapping.class);

		if (!noQueryParametersList.isPresent()) {
			parameters.add(SwaggerParameters.mimeType());
			parameters.add(SwaggerParameters.zip());
		};

		if (fullParameterList.isPresent()) {
			parameters.add(SwaggerParameters.analyticalmethod());
			parameters.add(SwaggerParameters.assemblage());
			parameters.add(SwaggerParameters.bBox());
			parameters.add(SwaggerParameters.characteristicName());
			parameters.add(SwaggerParameters.characteristicType());
			parameters.add(SwaggerParameters.countrycode());
			parameters.add(SwaggerParameters.countycode());
			parameters.add(SwaggerParameters.huc());
			parameters.add(SwaggerParameters.lat());
			parameters.add(SwaggerParameters.longitude());
			parameters.add(SwaggerParameters.minactivities());
			parameters.add(SwaggerParameters.minresults());
			parameters.add(SwaggerParameters.nldiurl());
			parameters.add(SwaggerParameters.organization());
			parameters.add(SwaggerParameters.pCode());
			parameters.add(SwaggerParameters.project());
			parameters.add(SwaggerParameters.providers());
			parameters.add(SwaggerParameters.sampleMedia());
			parameters.add(SwaggerParameters.siteid());
			parameters.add(SwaggerParameters.siteType());
			parameters.add(SwaggerParameters.sorted());
			parameters.add(SwaggerParameters.startDateHi());
			parameters.add(SwaggerParameters.startDateLo());
			parameters.add(SwaggerParameters.statecode());
			parameters.add(SwaggerParameters.subjectTaxonomicName());
			parameters.add(SwaggerParameters.within());
		}
                

		if (profileParameterActivity.isPresent()) {
			parameters.add(SwaggerParameters.dataProfile());
		}

		if (profileParameterResult.isPresent()) {
			parameters.add(SwaggerParameters.dataProfile());
		}

                if (profileParameterSummary.isPresent()) {
		    parameters.add(SwaggerParameters.bBox());
		    parameters.add(SwaggerParameters.countrycode());
		    parameters.add(SwaggerParameters.huc());
		    parameters.add(SwaggerParameters.lat());
		    parameters.add(SwaggerParameters.longitude());
		    parameters.add(SwaggerParameters.organization());
		    parameters.add(SwaggerParameters.providers());
		    parameters.add(SwaggerParameters.siteid());
		    parameters.add(SwaggerParameters.siteType());
		    parameters.add(SwaggerParameters.statecode());
		    parameters.add(SwaggerParameters.summaryYears());
		    parameters.add(SwaggerParameters.within());
                }
		if (postMapping.isPresent()) {
			parameters.add(SwaggerParameters.postParms());
		}

		if (!parameters.isEmpty()) {
			context.operationBuilder().parameters(parameters);
		}
	}

}
