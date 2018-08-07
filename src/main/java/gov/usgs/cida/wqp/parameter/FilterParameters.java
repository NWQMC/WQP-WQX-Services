package gov.usgs.cida.wqp.parameter;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.validation.BBox;
import gov.usgs.cida.wqp.validation.EnumLookup;
import gov.usgs.cida.wqp.validation.Fetch;
import gov.usgs.cida.wqp.validation.Lookup;
import gov.usgs.cida.wqp.validation.PointLocation;
import gov.usgs.cida.wqp.validation.WqpDate;

@JsonInclude(Include.NON_EMPTY)
@PointLocation
@Fetch(parameter=Parameters.NLDIURL)
public class FilterParameters {
	private static final Logger LOG = LoggerFactory.getLogger(FilterParameters.class);

	public static final String FORMAT_DATE = "MM-dd-yyyy";
	public static final int IN_CLAUSE_LIMIT = 1000;
	public static final String REGEX_FIPS_COUNTRY = "[A-Z]{2}";
	public static final String REGEX_FIPS_STATE = "(?:([A-Z]{2}):)?([0-9]{1,2})";
	public static final String REGEX_FIPS_COUNTY = "(?:([A-Z]{2}):)?([0-9]{1,2}):([0-9]{3}|N/A)";
	public static final String REGEX_SITEID = "[\\w]+\\-.*\\S";
	public static final String REGEX_POSITIVE_INT = "^\\d+$";
	public static final String REGEX_HUC = "(?:[0-9]{12})|(?:[0-9]{10})|(?:[0-9]{8})|(?:(?:[0-9]{2}){1,3}\\*?)";
	public static final String REGEX_PCODE = "[0-9]{5}";
	public static final String REGEX_MIMETYPES = "csv|tsv|tab|xlsx|xml|kml|kmz|json|geojson";
	public static final String REGEX_YES_NO = "yes|no";
	public static final String REGEX_ACEPT_ANYTHING = ".+";
	public static final String REGEX_HUC_WILDCARD_IN = "\\*";
	public static final String REGEX_HUC_WILDCARD_OUT = "";
	public static final String REGEX_DATA_PROFILE = String.join("|", Profile.getValues());
	public static final String REGEX_SUMMARY_YEARS = "1|5|all";


	private String activity;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The analyticalmethod is not between {min} and {max} occurances")
	private List<@URL(protocol="https",
		message="The value of analyticalmethod=${validatedValue} must be a valid HTTPS URL.")
		String> analyticalmethod;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The assemblage is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.ASSEMBLAGE) String> assemblage;

	@Valid
	private Command command;

	@BBox
	private BoundingBox bBox;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The characteristicName is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.CHARACTERISTIC_NAME) String> characteristicName;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The characteristicType is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.CHARACTERISTIC_TYPE) String> characteristicType;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The countrycode is not between {min} and {max} occurances")
	private List<@Pattern(
		regexp=REGEX_FIPS_COUNTRY,
		message="The value of countrycode=${validatedValue} must match the format {regexp}")
		String> countrycode;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The countycode is not between {min} and {max} occurances")
	private List<@Pattern(
		regexp=REGEX_FIPS_COUNTY,
		message="The value of countycode=${validatedValue} must match the format {regexp}")
		String> countycode;

	@EnumLookup(parameter=Parameters.DATA_PROFILE)
	private String dataProfile;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The huc is not between {min} and {max} occurances")
	private List<@Pattern(
		regexp=REGEX_HUC,
		message="The value of huc=${validatedValue} must match the format {regexp}")
		String> huc;

	@Range(min=-90, max=90, message="The value of lat=${validatedValue} is not between {min} and {max}.")
	private String lat;

	@Range(min=-180, max=180, message="The value of long=${validatedValue} is not between {min} and {max}.")
	private String longitude;

	@Pattern(regexp=REGEX_MIMETYPES, message="The value of mimeType=${validatedValue} must match the format {regexp}")
	private String mimeType;
	
	@Pattern(regexp=REGEX_SUMMARY_YEARS, message="The value of summaryYears=${validatedValue} must match the format {regexp}")
	private String summaryYears;

	@Pattern(regexp=REGEX_POSITIVE_INT, message="The value of minactivities=${validatedValue} must match the format {regexp}")
	private String minactivities;

	@Pattern(regexp=REGEX_POSITIVE_INT, message="The value of minresults=${validatedValue} must match the format {regexp}")
	private String minresults;

	@URL(protocol="https", message="The value of nldiurl=${validatedValue} must be a valid HTTPS URL.")
	private String nldiurl;

	//There is no validation on this parameter - it is built as part of the nlidurl processing and not exposed
	//to the outside world.
	private List<String> nldiSites;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The organization is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.ORGANIZATION) String> organization;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The pCode is not between {min} and {max} occurances")
	private List<@Pattern(
		regexp=REGEX_PCODE,
		message="The value of pCode=${validatedValue} must match the format {regexp}")
		String> pCode;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The project is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.PROJECT) String> project;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The providers is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.PROVIDERS) String> providers;

	private ResultIdentifier result;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The sampleMedia is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.SAMPLE_MEDIA) String> sampleMedia;

	private List<@Pattern(
		regexp=REGEX_SITEID,
		message="The value of siteid=${validatedValue} must match the format {regexp}")
		String> siteid;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The siteType is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.SITE_TYPE) String> siteType;

	//There is no validation on this parameter - it is part of the rest url building and not exposed
	//to the outside world.
	private String siteUrlBase;

	@Pattern(regexp=REGEX_YES_NO, message="The value of sorted=${validatedValue} must match the format {regexp}")
	private String sorted;

	@WqpDate(parameter=Parameters.START_DATE_HI, formatString=FORMAT_DATE)
	private String startDateHi;

	@WqpDate(parameter=Parameters.START_DATE_LO, formatString=FORMAT_DATE)
	private String startDateLo;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The statecode is not between {min} and {max} occurances")
	private List<@Pattern(
		regexp=REGEX_FIPS_STATE,
		message="The value of statecode=${validatedValue} must match the format {regexp}")
		String> statecode;

	@Size(min=0, max=IN_CLAUSE_LIMIT, message="The subjectTaxonomicName is not between {min} and {max} occurances")
	private List<@Lookup(parameter=Parameters.SUBJECT_TAXONOMIC_NAME) String> subjectTaxonomicName;

	@Min(value=0, message="The value of within=${validatedValue} must be a number greater than or equal to {value}.")
	private String within;

	@Pattern(regexp=REGEX_YES_NO, message="The value of zip=${validatedValue} must match the format {regexp}")
	private String zip;

	@JsonIgnore
	private Set<ConstraintViolation<FilterParameters>> validationErrors;



	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public List<String> getAnalyticalmethod() {
		return analyticalmethod;
	}
	public void setAnalyticalmethod(List<String> analyticalmethod) {
		this.analyticalmethod = analyticalmethod;
	}
	public List<String> getAssemblage() {
		return assemblage;
	}
	public void setAssemblage(List<String> assemblage) {
		this.assemblage = assemblage;
	}
	@JsonProperty("command.avoid")
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public BoundingBox getBBox() {
		return bBox;
	}
	public void setBBox(BoundingBox bBox) {
		this.bBox = bBox;
	}
	@JsonProperty("bbox")
	public String getBBoxForJson() {
		return null == bBox ? null : bBox.getSingle();
	}
	public List<String> getCharacteristicName() {
		return characteristicName;
	}
	public void setCharacteristicName(List<String> characteristicName) {
		this.characteristicName = characteristicName;
	}
	public List<String> getCharacteristicType() {
		return characteristicType;
	}
	public void setCharacteristicType(List<String> characteristicType) {
		this.characteristicType = characteristicType;
	}
	public List<String> getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(List<String> countrycode) {
		this.countrycode = countrycode;
	}
	public List<String> getCountycode() {
		return countycode;
	}
	public void setCountycode(List<String> countycode) {
		this.countycode = countycode;
	}
	public String getDataProfile() {
		return dataProfile;
	}
	public void setDataProfile(String dataProfile) {
		this.dataProfile = dataProfile;
	}
	public List<String> getHuc() {
		return huc;
	}
	public void setHuc(List<String> huc) {
		this.huc = huc;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLong() {
		return longitude;
	}
	public void setLong(String longitude) {
		this.longitude = longitude;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getMinactivities() {
		return minactivities;
	}
	public void setMinactivities(String minactivities) {
		this.minactivities = minactivities;
	}
	public String getMinresults() {
		return minresults;
	}
	public void setMinresults(String minresults) {
		this.minresults = minresults;
	}
	public String getNldiurl() {
		return nldiurl;
	}
	public void setNldiurl(String nldiurl) {
		this.nldiurl = nldiurl;
	}
	public List<String> getNldiSites() {
		return nldiSites;
	}
	public void setNldiSites(List<String> nldiSites) {
		this.nldiSites = nldiSites;
	}
	public List<String> getOrganization() {
		return organization;
	}
	public void setOrganization(List<String> organization) {
		this.organization = organization;
	}
	public List<String> getPCode() {
		return pCode;
	}
	public void setPCode(List<String> pCode) {
		this.pCode = pCode;
	}
	public List<String> getProject() {
		return project;
	}
	public void setProject(List<String> project) {
		this.project = project;
	}
	public List<String> getProviders() {
		return providers;
	}
	public void setProviders(List<String> providers) {
		this.providers = providers;
	}
	public ResultIdentifier getResult() {
		return result;
	}
	public void setResult(ResultIdentifier result) {
		this.result = result;
	}
	@JsonProperty("result")
	public String getResultJson() {
		return null == result ? null : result.getSingle();
	}
	public List<String> getSampleMedia() {
		return sampleMedia;
	}
	public void setSampleMedia(List<String> sampleMedia) {
		this.sampleMedia = sampleMedia;
	}
	public List<String> getSiteid() {
		return siteid;
	}
	public void setSiteid(List<String> siteid) {
		this.siteid = siteid;
	}
	public List<String> getSiteType() {
		return siteType;
	}
	public void setSiteType(List<String> siteType) {
		this.siteType = siteType;
	}
	public String getSiteUrlBase() {
		return siteUrlBase;
	}
	public void setSiteUrlBase(String siteUrlBase) {
		this.siteUrlBase = siteUrlBase;
	}
	public String getSorted() {
		return sorted;
	}
	public void setSorted(String sorted) {
		this.sorted = sorted;
	}
	public String getStartDateHi() {
		return startDateHi;
	}
	public void setStartDateHi(String startDateHi) {
		this.startDateHi = startDateHi;
	}
	public String getStartDateLo() {
		return startDateLo;
	}
	public void setStartDateLo(String startDateLo) {
		this.startDateLo = startDateLo;
	}
	public List<String> getStatecode() {
		return statecode;
	}
	public void setStatecode(List<String> statecode) {
		this.statecode = statecode;
	}
	public List<String> getSubjectTaxonomicName() {
		return subjectTaxonomicName;
	}
	public void setSubjectTaxonomicName(List<String> subjectTaxonomicName) {
		this.subjectTaxonomicName = subjectTaxonomicName;
	}
	public String getSummaryYears() {
		return summaryYears;
	}
	public void setSummaryYears(String summaryYears) {
		this.summaryYears = summaryYears;
	}
	public String getWithin() {
		return within;
	}
	public void setWithin(String within) {
		this.within = within;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public Set<ConstraintViolation<FilterParameters>> getValidationErrors() {
		return validationErrors;
	}
	public void setValidationErrors(Set<ConstraintViolation<FilterParameters>> validationErrors) {
		this.validationErrors = validationErrors;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return StringUtils.isBlank(activity)
				&& (null == analyticalmethod || analyticalmethod.isEmpty())
				&& (null == assemblage || assemblage.isEmpty())
				&& (null == bBox || StringUtils.isBlank(bBox.getSingle()))
				&& (null == characteristicName || characteristicName.isEmpty())
				&& (null == characteristicType || characteristicType.isEmpty())
				&& (null == command || StringUtils.isBlank(command.getAvoid()))
				&& (null == countrycode || countrycode.isEmpty())
				&& (null == countycode || countycode.isEmpty())
				&& StringUtils.isBlank(dataProfile)
				&& (null == huc || huc.isEmpty())
				&& StringUtils.isBlank(lat)
				&& StringUtils.isBlank(longitude)
				&& StringUtils.isBlank(mimeType)
				&& StringUtils.isBlank(minactivities)
				&& StringUtils.isBlank(minresults)
				&& StringUtils.isBlank(nldiurl)
				&& (null == organization || organization.isEmpty())
				&& (null == pCode || pCode.isEmpty())
				&& (null == project || project.isEmpty())
				&& (null == providers || providers.isEmpty())
				&& (null == result || StringUtils.isBlank(result.getSingle()))
				&& (null == sampleMedia || sampleMedia.isEmpty())
				&& (null == siteid || siteid.isEmpty())
				&& (null == siteType || siteType.isEmpty())
				&& StringUtils.isBlank(sorted)
				&& StringUtils.isBlank(startDateHi)
				&& StringUtils.isBlank(startDateLo)
				&& (null == statecode || statecode.isEmpty())
				&& (null == subjectTaxonomicName || subjectTaxonomicName.isEmpty())
				&& StringUtils.isBlank(summaryYears)
				&& StringUtils.isBlank(within)
				&& StringUtils.isBlank(zip);
	}

	@JsonIgnore
	public boolean isValid() {
		return null == validationErrors || validationErrors.isEmpty();
	}

	@JsonIgnore
	public String toJson() {
		String rtn = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			rtn = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			rtn = "Error serializing filter";
			LOG.info(rtn, e);
		}
		return rtn;
	}

}
