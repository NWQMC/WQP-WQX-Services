package gov.usgs.cida.wqp.springinit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.parameter.HashMapParameterHandler;
import gov.usgs.cida.wqp.parameter.IParameterHandler;
import gov.usgs.cida.wqp.parameter.Parameters;
import gov.usgs.cida.wqp.parameter.transform.ParameterTransformer;
import gov.usgs.cida.wqp.parameter.transform.SplitAndReplaceTransformer;
import gov.usgs.cida.wqp.service.CodesService;
import gov.usgs.cida.wqp.service.FetchService;
import gov.usgs.cida.wqp.validation.AbstractValidator;
import gov.usgs.cida.wqp.validation.BoundedFloatingPointValidator;
import gov.usgs.cida.wqp.validation.DateFormatValidator;
import gov.usgs.cida.wqp.validation.FetchValidator;
import gov.usgs.cida.wqp.validation.LatLonBoundingBoxValidator;
import gov.usgs.cida.wqp.validation.LatitudeValidator;
import gov.usgs.cida.wqp.validation.LongitudeValidator;
import gov.usgs.cida.wqp.validation.LookupValidator;
import gov.usgs.cida.wqp.validation.RegexValidator;

@Configuration
public class ParameterValidationConfig {
	
	@Autowired
	CodesService codesService;

	@Autowired
	FetchService fetchService;

	public static final String NLDI_WQP_FEATURE_IDENTIFIER = "identifier";
	public static final String FORMAT_DATE = "MM-dd-yyyy";

	public static final String REGEX_FIPS_COUNTRY = "[A-Z]{2}";
	public static final String REGEX_FIPS_STATE = "(?:([A-Z]{2}):)?([0-9]{1,2})";
	public static final String REGEX_FIPS_COUNTY = "(?:([A-Z]{2}):)?([0-9]{1,2}):([0-9]{3}|N/A)";
	public static final String REGEX_SITEID = "[\\w]+\\-.*\\S";
	public static final String REGEX_POSITIVE_INT = "^\\d+$";
	public static final String REGEX_HUC = "(?:[0-9]{12})|(?:[0-9]{10})|(?:[0-9]{8})|(?:(?:[0-9]{2}){1,3}\\*?)";
	public static final String REGEX_PCODE = "[0-9]{5}";
	public static final String REGEX_MIMETYPES = "csv|tsv|tab|xlsx|xml|kml|kmz|json|geojson";
	public static final String REGEX_AVOID = "NWIS|STORET";
	public static final String REGEX_YES_NO = "yes|no";
	public static final String REGEX_ACEPT_ANYTHING = ".+";
	public static final String REGEX_HUC_WILDCARD_IN = "\\*";
	public static final String REGEX_HUC_WILDCARD_OUT = "";
	public static final String REGEX_DATA_PROFILE = String.join("|", Profile.getValues());

	@Bean
	public RegexValidator<String> activityValidator() {
		return new RegexValidator<String>(Parameters.ACTIVITY, AbstractValidator.DEFAULT_MIN_OCCURS, 1, AbstractValidator.DEFAULT_DELIMITER, REGEX_ACEPT_ANYTHING);
	}

	@Bean
	public RegexValidator<String> analyticalMethodValidator() {
		return new RegexValidator<String>(Parameters.ANALYTICAL_METHOD, REGEX_ACEPT_ANYTHING);
	}

	@Bean
	public LookupValidator assemblageValidator() {
		// semicolon list of string assemblage names
		return new LookupValidator(codesService, Parameters.ASSEMBLAGE);
	}

	@Bean
	public RegexValidator<String[]> avoidValidator(){
		// semicolon list of databases to exclude as 'command.avoid'
		return new RegexValidator<String[]>(Parameters.AVOID, REGEX_AVOID);
	}

	@Bean
	public LatLonBoundingBoxValidator bboxValidator() {
		// comma list of four float values
		return new LatLonBoundingBoxValidator(Parameters.BBOX);
	}

	@Bean
	public LookupValidator characteristicNameValidator() {
		// semicolon list of string characteristic names
		return new LookupValidator(codesService, Parameters.CHARACTERISTIC_NAME);
	}

	@Bean
	public LookupValidator characteristicTypeValidator() {
		// semicolon list of string characteristic types
		return new LookupValidator(codesService, Parameters.CHARACTERISTIC_TYPE);
	}

	@Bean
	public RegexValidator<String[]> countryValidator() {
		// semicolon list of string country codes
		return new RegexValidator<String[]>(Parameters.COUNTRY, REGEX_FIPS_COUNTRY);
	}

	@Bean
	public RegexValidator<String[]> countyValidator() {
		// semicolon list of country:state:county code strings
		return new RegexValidator<String[]>(Parameters.COUNTY, REGEX_FIPS_COUNTY);
	}

	@Bean
	public RegexValidator<String[]> dataProfileValidator() {
		// semicolon list of country:state:county code strings
		return new RegexValidator<String[]>(Parameters.DATA_PROFILE, REGEX_DATA_PROFILE);
	}

	@Bean
	public AbstractValidator<String[]> hucValidator() {
		// semicolon list of HUC codes
		AbstractValidator<String[]> hucValidator = new RegexValidator<String[]>(Parameters.HUC, REGEX_HUC);
		ParameterTransformer<String[]> hucTransformer = new SplitAndReplaceTransformer(AbstractValidator.DEFAULT_DELIMITER, REGEX_HUC_WILDCARD_IN, REGEX_HUC_WILDCARD_OUT);
		hucValidator.setTransformer(hucTransformer);
		return hucValidator;
	}

	@Bean
	public LatitudeValidator latitudeValidator() {
		// one float value
		return new LatitudeValidator(Parameters.LATITUDE);
	}

	@Bean
	public LongitudeValidator longitudeValidator() {
		// one float value
		return new LongitudeValidator(Parameters.LONGITUDE);
	}

	@Bean
	public RegexValidator<String[]> mimeTypeValidator() {
		// one string mimetype
		return new RegexValidator<String[]>(Parameters.MIMETYPE, 1, 1, null, REGEX_MIMETYPES);
	}

	@Bean
	public RegexValidator<String[]> minResultsValidator() {
		// one int value 
		return new RegexValidator<String[]>(Parameters.MIN_RESULTS, REGEX_POSITIVE_INT);
	}

	@Bean
	public RegexValidator<String[]> minActivitiesValidator() {
		// one int value 
		return new RegexValidator<String[]>(Parameters.MIN_ACTIVITIES, REGEX_POSITIVE_INT);
	}

	@Bean
	public FetchValidator nldiurlValidator() {
		// one NLDI navigation URL 
		return new FetchValidator(NLDI_WQP_FEATURE_IDENTIFIER, fetchService, Parameters.NLDIURL);
	}

	@Bean
	public LookupValidator organizationValidator() {
		// semicolon list of string ORG names
		return new LookupValidator(codesService, Parameters.ORGANIZATION);
	}

	@Bean
	public RegexValidator<String[]> parameterCodeValidator() {
		// semicolon list of 5digit pCodes
		return new RegexValidator<String[]>(Parameters.PCODE, REGEX_PCODE);
	}

	@Bean
	public LookupValidator projectValidator() {
		// semicolon list of string project ids
		return new LookupValidator(codesService, Parameters.PROJECT);
	}

	@Bean
	public LookupValidator providerValidator() {
		// semicolon list of databases to include
		return new LookupValidator(codesService, Parameters.PROVIDERS);
	}

	@Bean
	public RegexValidator<String> resultValidator() {
		return new RegexValidator<String>(Parameters.RESULT, AbstractValidator.DEFAULT_MIN_OCCURS, 1, "-", REGEX_ACEPT_ANYTHING);
	}

	@Bean
	public LookupValidator sampleMediaValidator() {
		// semicolon list of string media type names
		return new LookupValidator(codesService, Parameters.SAMPLE_MEDIA);
	}

	@Bean
	public LookupValidator siteTypeValidator() {
		// semicolon list of string site type names
		return new LookupValidator(codesService, Parameters.SITE_TYPE);
	}

	@Bean
	public RegexValidator<String[]> siteIdValidator() {
		// agency-site string
		return new RegexValidator<String[]>(Parameters.SITEID, AbstractValidator.DEFAULT_MIN_OCCURS, AbstractValidator.UNBOUNDED, AbstractValidator.DEFAULT_DELIMITER, REGEX_SITEID);
	}

	@Bean
	public RegexValidator<String[]> sortedValidator() {
		// a string
		return new RegexValidator<String[]>(Parameters.SORTED, REGEX_YES_NO);
	}

	@Bean
	public DateFormatValidator startDateHiValidator() {
		// one string date MM-DD-YYYY
		return new DateFormatValidator(Parameters.START_DATE_HI, FORMAT_DATE);
	}

	@Bean
	public DateFormatValidator startDateLoValidator() {
		// one string date MM-DD-YYYY
		return new DateFormatValidator(Parameters.START_DATE_LO, FORMAT_DATE);
	}

	@Bean
	public RegexValidator<String[]> stateValidator() {
		// country:state code string
		return new RegexValidator<String[]>(Parameters.STATE, REGEX_FIPS_STATE);
	}

	@Bean
	public LookupValidator subjectTaxonomicNameValidator() {
		// semicolon list of string ubjectTaxonomicName names
		return new LookupValidator(codesService, Parameters.SUBJECT_TAXONOMIC_NAME);
	}

	@Bean
	public BoundedFloatingPointValidator withinValidator() {
		// one float value
		return new BoundedFloatingPointValidator(Parameters.WITHIN, 0, Double.MAX_VALUE);
	}

	@Bean
	public RegexValidator<String[]> zipValidator() {
		// one string 'yes' or omitted
		return new RegexValidator<String[]>(Parameters.ZIP, 0, 1, null, REGEX_YES_NO);
	}

	@Bean
	public Map<Parameters, AbstractValidator<?>> validatorMap() {
		Map<Parameters, AbstractValidator<?>> validatorMap = new HashMap<Parameters, AbstractValidator<?>>();
		validatorMap.put(Parameters.ACTIVITY, activityValidator());
		validatorMap.put(Parameters.ANALYTICAL_METHOD, analyticalMethodValidator());
		validatorMap.put(Parameters.ASSEMBLAGE, assemblageValidator());
		validatorMap.put(Parameters.AVOID, avoidValidator());
		validatorMap.put(Parameters.BBOX, bboxValidator());
		validatorMap.put(Parameters.CHARACTERISTIC_NAME, characteristicNameValidator());
		validatorMap.put(Parameters.CHARACTERISTIC_TYPE, characteristicTypeValidator());
		validatorMap.put(Parameters.COUNTRY, countryValidator());
		validatorMap.put(Parameters.COUNTY, countyValidator());
		validatorMap.put(Parameters.DATA_PROFILE, dataProfileValidator());
		validatorMap.put(Parameters.HUC, hucValidator());
		validatorMap.put(Parameters.LATITUDE, latitudeValidator());
		validatorMap.put(Parameters.LONGITUDE, longitudeValidator());
		validatorMap.put(Parameters.MIMETYPE, mimeTypeValidator());
		validatorMap.put(Parameters.MIN_ACTIVITIES, minActivitiesValidator());
		validatorMap.put(Parameters.MIN_RESULTS, minResultsValidator());
		validatorMap.put(Parameters.NLDIURL, nldiurlValidator());
		validatorMap.put(Parameters.ORGANIZATION, organizationValidator());
		validatorMap.put(Parameters.PCODE, parameterCodeValidator());
		validatorMap.put(Parameters.PROJECT, projectValidator());
		validatorMap.put(Parameters.PROVIDERS, providerValidator());
		validatorMap.put(Parameters.RESULT, resultValidator());
		validatorMap.put(Parameters.SAMPLE_MEDIA, sampleMediaValidator());
		validatorMap.put(Parameters.SITE_TYPE, siteTypeValidator());
		validatorMap.put(Parameters.SITEID, siteIdValidator());
		validatorMap.put(Parameters.SORTED, sortedValidator());
		validatorMap.put(Parameters.START_DATE_HI, startDateHiValidator());
		validatorMap.put(Parameters.START_DATE_LO, startDateLoValidator());
		validatorMap.put(Parameters.STATE, stateValidator());
		validatorMap.put(Parameters.SUBJECT_TAXONOMIC_NAME, subjectTaxonomicNameValidator());
		validatorMap.put(Parameters.WITHIN, withinValidator());
		validatorMap.put(Parameters.ZIP, zipValidator());
	return validatorMap;
	}

	@Bean
	public IParameterHandler hashMapParameterHandler() {
		return new HashMapParameterHandler(validatorMap());
	}

}
