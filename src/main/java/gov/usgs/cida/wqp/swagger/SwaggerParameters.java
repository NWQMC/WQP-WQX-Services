package gov.usgs.cida.wqp.swagger;

import static gov.usgs.cida.wqp.util.MimeType.csv;
import static gov.usgs.cida.wqp.util.MimeType.geojson;
import static gov.usgs.cida.wqp.util.MimeType.json;
import static gov.usgs.cida.wqp.util.MimeType.kml;
import static gov.usgs.cida.wqp.util.MimeType.kmz;
import static gov.usgs.cida.wqp.util.MimeType.text;
import static gov.usgs.cida.wqp.util.MimeType.tsv;
import static gov.usgs.cida.wqp.util.MimeType.xlsx;
import static gov.usgs.cida.wqp.util.MimeType.xml;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import gov.usgs.cida.wqp.mapping.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.Parameter;

@Component
public class SwaggerParameters {

	public static final String MONITORING_LOCATION_DESCRIPTION = "Case-sensitive Monitoring Location (Site ID).";
	public static final String ORGANIZATION_DESCRIPTION = "Case-sensitive Organization Identifier.";
	public static final String PROJECT_IDENTIFIER_DESCRIPTION = "Case-sensitive Project Identifier.";
	public static final String ACTIVITY_DESCRIPTION = "Case-sensitive Activity Identifier.";
	public static final String RESULT_DESCRIPTION = "Case-sensitive Result Identifier.";

	private SwaggerParameters() {}

	public static Parameter activity() {
		return new ParameterBuilder()
				.name("activity")
				.description(ACTIVITY_DESCRIPTION)
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter analyticalmethod() {
		return new ParameterBuilder()
				.name("analyticalmethod")
				.description("One or more Analytical Methods.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter assemblage() {
		return new ParameterBuilder()
				.name("assemblage")
				.description("One or more Assemblage.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter bBox() {
		return new ParameterBuilder()
				.name("bBox")
				.description("Western-most longitude, Southern-most latitude, Eastern-most longitude, and Northern-most longitude separated by commas,"
						+ "expressed in decimal degrees, WGS84, and longitudes west of Greenwich are negative. (Example: bBox=-92.8,44.2,-88.9,46.0)")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter characteristicName() {
		return new ParameterBuilder()
				.name("characteristicName")
				.description("One or more case-sensitive Characteristic Names.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter characteristicType() {
		return new ParameterBuilder()
				.name("characteristicType")
				.description("One or more case-sensitive Characteristic Types (groupings).")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter countrycode() {
		return new ParameterBuilder()
				.name("countrycode")
				.description("One or more two-character Federal Information Processing Standard (FIPS) Country Codes.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter countycode() {
		return new ParameterBuilder()
				.name("countycode")
				.description("One or more two-character Federal Information Processing Standard (FIPS) Country Code, followed by a URL-encoded colon (\"%3A\"),"
						+ " followed by a two-digit FIPS State Code, followed by a URL-encoded colon (\"%3A\"), followed by a three-digit FIPS County Code")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter dataProfile() {
		return new ParameterBuilder()
				.name("dataProfile")
				.description("Data Profile (column set) of the download. For use with the results endpoint.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList(Profile.BIOLOGICAL.toString(), Profile.PC_RESULT.toString(),
						Profile.NARROW_RESULT.toString(), Profile.ACTIVITY.toString(), Profile.ACTIVITY_ALL.toString()), "LIST"))
				.build();
	}

	public static Parameter huc() {
		return new ParameterBuilder()
				.name("huc")
				.description("One or more two-, four-, six-, or eight-digit Hydrologic Units.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter lat() {
		return new ParameterBuilder()
				.name("lat")
				.description("Latitude for radial search, expressed in decimal degrees, WGS84.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter longitude() {
		return new ParameterBuilder()
				.name("long")
				.description("Longitude for radial search, expressed in decimal degrees, WGS84.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter mimeType() {
		return new ParameterBuilder()
				.name("mimeType")
				.description("Can be used in place of the 'Accept' header. See 'Response Content Type' for the valid subset of these values.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList(csv.getExtension(), tsv.getExtension(), xml.getExtension(), json.getExtension(),
						xlsx.getExtension(), kml.getExtension(), kmz.getExtension(), geojson.getExtension(), text.getExtension()), "LIST"))
				.build();
	}

	public static Parameter minactivities() {
		return new ParameterBuilder()
				.name("minactivities")
				.description("The minimum number of Sampling Activities at a site for inclusion in the dataset.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter minresults() {
		return new ParameterBuilder()
				.name("minresults")
				.description("The minimum number of Results at a site for inclusion in the dataset.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter nldiurl() {
		return new ParameterBuilder()
				.name("nldiurl")
				.description("The navigation query from NLDI for a list of sites to select data from.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter organization() {
		return new ParameterBuilder()
				.name("organization")
				.description("One or more case-sensitive Organization Identifiers.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter pCode() {
		return new ParameterBuilder()
				.name("pCode")
				.description("One or more five-digit USGS parameter codes.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter project() {
		return new ParameterBuilder()
				.name("project")
				.description("One or more case-sensitive Project Identifiers.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter providers() {
		return new ParameterBuilder()
				.name("providers")
				.description("One or more case-sensitive Providers.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter result() {
		return new ParameterBuilder()
				.name("result")
				.description(RESULT_DESCRIPTION)
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter sampleMedia() {
		return new ParameterBuilder()
				.name("sampleMedia")
				.description("One or more case-sensitive Sample Media.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter siteid() {
		return new ParameterBuilder()
				.name("siteid")
				.description("One or more case-sensitive Site IDs.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter siteType() {
		return new ParameterBuilder()
				.name("siteType")
				.description("One or more case-sensitive Site Types.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter sorted() {
		return new ParameterBuilder()
				.name("sorted")
				.description("Should the data be sorted. Response times may be improved by not sorting.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList("yes", "no"), "LIST"))
				.build();
	}

	public static Parameter startDateHi() {
		return new ParameterBuilder()
				.name("startDateHi")
				.description("Date of last desired data-collection activity, expressed as MM-DD-YYYY")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter startDateLo() {
		return new ParameterBuilder()
				.name("startDateLo")
				.description("Date of earliest desired data-collection activity, expressed as MM-DD-YYYY")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter statecode() {
		return new ParameterBuilder()
				.name("statecode")
				.description("One or more two-character Federal Information Processing Standard (FIPS) country code, followed by a URL-encoded colon (\"%3A\"), followed by a two-digit FIPS state code.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}

	public static Parameter subjectTaxonomicName() {
		return new ParameterBuilder()
				.name("subjectTaxonomicName")
				.description("One or more case-sensitive Taxonomic Names.")
				.modelRef(new ModelRef("", new ModelRef("string")))
				.parameterType("query")
				.required(false)
				.allowMultiple(true)
				.build();
	}
	
	public static Parameter summaryYears() {
		return new ParameterBuilder()
				.name("summaryYears")
				.description("Select summary data for 1, 5, or all years.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList("1", "5", "all"), "LIST"))
				.build();
	}

	public static Parameter within() {
		return new ParameterBuilder()
				.name("within")
				.description("Distance for radial search, expressed in decimal miles.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.build();
	}

	public static Parameter zip() {
		return new ParameterBuilder()
				.name("zip")
				.description("Should the data be compressed. Compression often greatly increases throughput, thus expediting the request.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList("yes", "no"), "LIST"))
				.build();
	}

	public static Parameter postParms() {
		return new ParameterBuilder()
				.name("postParms")
				.description("A JSON object with required search parameters.")
				.modelRef(new ModelRef("PostParms"))
				.parameterType("body")
				.required(true)
				.build();
	}

}
