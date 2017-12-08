package gov.usgs.cida.wqp.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Profile {

	STATION ("station", "station"),
	SIMPLE_STATION ("simplestation", "simplestation"),
	BIOLOGICAL ("biological", "biologicalresult"),
	PC_RESULT ("pcresult", "result"),
	ACTIVITY ("activity", "activity"),
	ACTIVITY_METRIC ("activityMetric", "activitymetric"),
	RES_DETECT_QNT_LMT ("resDetectQntLmt", "resdetectqntlmt"),
	NARROW_RESULT ("narrowResult", "narrowresult"),
	PROJECT ("project", "project");

	private final String name;
	private final String baseFileName;
	private static Map<String, Profile> profileMap = new HashMap<>();
	
	static {
		for (Profile profile : Profile.values()) {
			profileMap.put(profile.name, profile);
		}
	}

	private Profile(String name, String baseFileName) {
		this.name = name;
		this.baseFileName = baseFileName;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getBaseFileName() {
		return baseFileName;
	}

	public static Profile fromString(String profile) {
		if (profile == null || profile.isEmpty()) {
			return null;
		}
		try {
			return profileMap.get(profile);
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

	public static Set<String> getValues() {
		return profileMap.keySet();
	}
}
