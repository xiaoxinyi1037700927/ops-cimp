package com.sinosoft.ops.cimp.util.word.pattern;

import com.aspose.words.*;
import com.sinosoft.ops.cimp.export.processor.EvaluationAttrValueProcessor;
import com.sinosoft.ops.cimp.export.processor.FamilyOrgAndJobAttrValueProcessor;
import com.sinosoft.ops.cimp.export.processor.HornorAttrValueProcessor;
import com.sinosoft.ops.cimp.export.processor.PositionAttrValueProcessor;
import com.sinosoft.ops.cimp.util.StringUtil;
import com.sinosoft.ops.cimp.util.word.pattern.processor.*;
import com.sinosoft.ops.cimp.util.word.pattern.xinjiang.*;
import com.sinosoft.ops.cimp.service.export.ExportService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rain chen on 2017/9/20.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0 Copyright (C) 2017. SinSoft All Rights Received
 */
public class AbstractExportWordXinJiang {
    private static final Logger logger = LoggerFactory.getLogger(AbstractExportWordXinJiang.class);
	// 属性-属性规则
	private Map<String, AttrValue> attrValueMap = new LinkedHashMap<String, AttrValue>(59);

	// 属性-解析器
	private Map<String, AttrValueProcessor> attrValueProcessorMap = new HashMap<String, AttrValueProcessor>(59);

	// 属性值执行上下文
	private Map<String, Object> attrValueContext = new LinkedHashMap<String, Object>();

	// 需要添加的字符数
	private List<Integer> addLines = new ArrayList<Integer>();

	public AbstractExportWordXinJiang(String wordType) {
		switch (wordType) {
		case "gbrmb":
			//储存word类型
			this.attrValueContext.put("wordType", "gbrmb");
			// 放入 属性 - 属性获取处理器
			attrValueMap.put(new NameAttrValue().getKey(), new NameAttrValue());
			attrValueMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValue());
			attrValueMap.put(new GenderAttrValue().getKey(), new GenderAttrValue());
			attrValueMap.put(new BirthdayAttrValue().getKey(), new BirthdayAttrValue());
			attrValueMap.put(new NationAttrValue().getKey(), new NationAttrValue());
			attrValueMap.put(new NativeAttrValue().getKey(), new NativeAttrValue());
			attrValueMap.put(new BirthPlaceAttrValue().getKey(), new BirthPlaceAttrValue());
			attrValueMap.put(new PartyDateAttrValue().getKey(), new PartyDateAttrValue());
			attrValueMap.put(new JobDateAttrValue().getKey(), new JobDateAttrValue());
			attrValueMap.put(new HealthyAttrValue().getKey(), new HealthyAttrValue());
			attrValueMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValue());
			attrValueMap.put(new SpecialtyAttrValue().getKey(), new SpecialtyAttrValue());
			attrValueMap.put(new FtDiplomaAndDegreeAttrValue().getKey(), new FtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new FtInstituteAndSpecialtyAttrValue().getKey(), new FtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PtDiplomaAndDegreeAttrValue().getKey(), new PtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new PtInstituteAndSpecialtyAttrValue().getKey(), new PtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PositionAttrValue().getKey(), new PositionAttrValue());
			attrValueMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValue());
			attrValueMap.put(new HornorAttrValue().getKey(), new HornorAttrValue());
			attrValueMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValue());
			attrValueMap.put(new FamilyRelAttrValue().getKey(), new FamilyRelAttrValue());
			attrValueMap.put(new FamilyNameAttrValue().getKey(), new FamilyNameAttrValue());
			attrValueMap.put(new FamilyAgeAttrValue().getKey(), new FamilyAgeAttrValue());
			attrValueMap.put(new FamilyPartyAttrValue().getKey(), new FamilyPartyAttrValue());
			attrValueMap.put(new FamilyOrgAndJobAttrValue().getKey(), new FamilyOrgAndJobAttrValue());
			// 放入 属性 - 属性值处理器
			attrValueProcessorMap.put(new NameAttrValue().getKey(), new NameAttrValueProcessor());
			attrValueProcessorMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValueProcessor());
			attrValueProcessorMap.put(new NationAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new NativeAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new BirthPlaceAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new SpecialtyAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new FtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
			attrValueProcessorMap.put(new PtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
//			attrValueProcessorMap.put(new PositionAttrValue().getKey(), new PositionAttrValueProcessor());
//			attrValueProcessorMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValueProcessor());
//			attrValueProcessorMap.put(new HornorAttrValue().getKey(), new HornorAttrValueProcessor());
//			attrValueProcessorMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValueProcessor());
//			attrValueProcessorMap.put(new FamilyOrgAndJobAttrValue().getKey(), new FamilyOrgAndJobAttrValueProcessor());
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "gbrmbfmid":
			//储存word类型
			this.attrValueContext.put("wordType", "gbrmb");
			// 放入 属性 - 属性获取处理器
			attrValueMap.put(new NameAttrValue().getKey(), new NameAttrValue());
			attrValueMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValue());
			attrValueMap.put(new GenderAttrValue().getKey(), new GenderAttrValue());
			attrValueMap.put(new BirthdayAttrValue().getKey(), new BirthdayAttrValue());
			attrValueMap.put(new NationAttrValue().getKey(), new NationAttrValue());
			attrValueMap.put(new NativeAttrValue().getKey(), new NativeAttrValue());
			attrValueMap.put(new BirthPlaceAttrValue().getKey(), new BirthPlaceAttrValue());
			attrValueMap.put(new PartyDateAttrValue().getKey(), new PartyDateAttrValue());
			attrValueMap.put(new JobDateAttrValue().getKey(), new JobDateAttrValue());
			attrValueMap.put(new HealthyAttrValue().getKey(), new HealthyAttrValue());
			attrValueMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValue());
			attrValueMap.put(new SpecialtyAttrValue().getKey(), new SpecialtyAttrValue());
			attrValueMap.put(new FtDiplomaAndDegreeAttrValue().getKey(), new FtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new FtInstituteAndSpecialtyAttrValue().getKey(), new FtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PtDiplomaAndDegreeAttrValue().getKey(), new PtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new PtInstituteAndSpecialtyAttrValue().getKey(), new PtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PositionAttrValue().getKey(), new PositionAttrValue());
			attrValueMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValue());
			attrValueMap.put(new HornorAttrValue().getKey(), new HornorAttrValue());
			attrValueMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValue());
			attrValueMap.put(new FamilyRelAttrValue().getKey(), new FamilyRelAttrValue());
			attrValueMap.put(new FamilyNameAttrValue().getKey(), new FamilyNameAttrValue());
			attrValueMap.put(new FamilyAgeAttrValue().getKey(), new FamilyAgeAttrValue());
			attrValueMap.put(new FamilyPartyAttrValue().getKey(), new FamilyPartyAttrValue());
			attrValueMap.put(new FamilyOrgAndJobAndIdAttrValue().getKey(), new FamilyOrgAndJobAndIdAttrValue());
			
			// 放入 属性 - 属性值处理器
			attrValueProcessorMap.put(new NameAttrValue().getKey(), new NameAttrValueProcessor());
			attrValueProcessorMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValueProcessor());
			attrValueProcessorMap.put(new NationAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new NativeAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new BirthPlaceAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new SpecialtyAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new FtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
			attrValueProcessorMap.put(new PtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
//			attrValueProcessorMap.put(new PositionAttrValue().getKey(), new PositionAttrValueProcessor());
//			attrValueProcessorMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValueProcessor());
//			attrValueProcessorMap.put(new HornorAttrValue().getKey(), new HornorAttrValueProcessor());
//			attrValueProcessorMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValueProcessor());
//			attrValueProcessorMap.put(new FamilyOrgAndJobAndIdAttrValue().getKey(), new FamilyOrgAndJobAttrValueProcessor());
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "xxcjb":
			//储存word类型
			this.attrValueContext.put("wordType", "xxcjb");
			// 放入 属性 - 属性获取处理器
			attrValueMap.put(new NameAttrValue().getKey(), new NameAttrValue());
			attrValueMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValue());
			attrValueMap.put(new GenderAttrValue().getKey(), new GenderAttrValue());
			attrValueMap.put(new BirthdayAttrValue().getKey(), new BirthdayAttrValue());
			attrValueMap.put(new NationAttrValue().getKey(), new NationAttrValue());
			attrValueMap.put(new NativeAttrValue().getKey(), new NativeAttrValue());
			attrValueMap.put(new IndividualStatusAttrValue().getKey(), new IndividualStatusAttrValue());
			attrValueMap.put(new IdNumAttrValue().getKey(), new IdNumAttrValue());
			attrValueMap.put(new DepartmentFullNameAttrValue().getKey(), new DepartmentFullNameAttrValue());
			attrValueMap.put(new HomeAddressAttrValue().getKey(), new HomeAddressAttrValue());
			attrValueMap.put(new HomeTelephoneAttrValue().getKey(), new HomeTelephoneAttrValue());
			attrValueMap.put(new PostCodeAttrValue().getKey(), new PostCodeAttrValue());
			attrValueMap.put(new WorkTelephone1AttrValue().getKey(), new WorkTelephone1AttrValue());
			attrValueMap.put(new WorkTelephone2AttrValue().getKey(), new WorkTelephone2AttrValue());
			attrValueMap.put(new MobilePhone1AttrVAlue().getKey(), new MobilePhone1AttrVAlue());
			attrValueMap.put(new MobilePhone2AttrVAlue().getKey(), new MobilePhone2AttrVAlue());
			attrValueMap.put(new PoliticalStatusAttrValue().getKey(), new PoliticalStatusAttrValue());
			attrValueMap.put(new BecomePartyDateAttrValue().getKey(), new BecomePartyDateAttrValue());
			attrValueMap.put(new PartyFullNameAttrAvlue().getKey(), new PartyFullNameAttrAvlue());
			attrValueMap.put(new LoseTrackDateAttrValue().getKey(), new LoseTrackDateAttrValue());
			attrValueMap.put(new IsStopPartyAttrValue().getKey(), new IsStopPartyAttrValue());
			attrValueMap.put(new IsFlowPartyAttrValue().getKey(), new IsFlowPartyAttrValue());
			attrValueMap.put(new FlowOutDateAttrValue().getKey(), new FlowOutDateAttrValue());
			attrValueMap.put(new FlowOutAreaAttrValue().getKey(), new FlowOutAreaAttrValue());
			attrValueMap.put(new RetireTypeAttrValue().getKey(), new RetireTypeAttrValue());
			attrValueMap.put(new RetireDateAttrValue().getKey(), new RetireDateAttrValue());
			
			attrValueMap.put(new XxcjPartyDateAttrValue().getKey(), new XxcjPartyDateAttrValue());
			attrValueMap.put(new JobDateAttrValue().getKey(), new JobDateAttrValue());
//			attrValueMap.put(new HealthyAttrValue().getKey(), new HealthyAttrValue());
			attrValueMap.put(new FtDiplomaAndDegreeAttrValue().getKey(), new FtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new FtInstituteAndSpecialtyAttrValue().getKey(), new FtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PtDiplomaAndDegreeAttrValue().getKey(), new PtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new PtInstituteAndSpecialtyAttrValue().getKey(), new PtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PositionAttrValue().getKey(), new PositionAttrValue());
//			attrValueMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValue());

			// 放入 属性 - 属性值处理器
			attrValueProcessorMap.put(new NameAttrValue().getKey(), new NameAttrValueProcessor());
			attrValueProcessorMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValueProcessor());
			attrValueProcessorMap.put(new NationAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new NativeAttrValue().getKey(), new GenericNameAttrValueProcessor());
//			attrValueProcessorMap.put(new IndividualStatusAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new FtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
			attrValueProcessorMap.put(new PtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
//			attrValueProcessorMap.put(new PositionAttrValue().getKey(), new PositionAttrValueProcessor());
//			attrValueProcessorMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValueProcessor());
//			attrValueProcessorMap.put(new FamilyOrgAndJobAttrValue().getKey(), new FamilyOrgAndJobAttrValueProcessor());
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "dwdzzxxcjb":
			//储存word类型
			this.attrValueContext.put("wordType", "dwdzzxxcjb");
			attrValueMap.put(new OrganizationFullNameAttrValue().getKey(), new OrganizationFullNameAttrValue());
			attrValueMap.put(new OrganizationCodeAttrValue().getKey(), new OrganizationCodeAttrValue());
			attrValueMap.put(new LegalRepresentativeAttrValue().getKey(), new LegalRepresentativeAttrValue());
			attrValueMap.put(new UnitPropertiesAttrValue().getKey(), new UnitPropertiesAttrValue());
			attrValueMap.put(new OrganizationCategoryAttrValue().getKey(), new OrganizationCategoryAttrValue());
			attrValueMap.put(new PartyOrganizationFromDepAttrValue().getKey(), new PartyOrganizationFromDepAttrValue());
			attrValueMap.put(new PartyCategoryAttrValue().getKey(), new PartyCategoryAttrValue());
			attrValueMap.put(new SartySecretaryAttrValue().getKey(), new SartySecretaryAttrValue());
			attrValueMap.put(new PartyContactAttrValue().getKey(), new PartyContactAttrValue());
			attrValueMap.put(new PartyContactOphAttrValue().getKey(), new PartyContactOphAttrValue());
			attrValueMap.put(new PartyContactCphAttrValue().getKey(), new PartyContactCphAttrValue());
			attrValueMap.put(new DanweiOfPartyAttrValue().getKey(), new DanweiOfPartyAttrValue());
			
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "dwxxcjb":
			//储存word类型
			this.attrValueContext.put("wordType", "dwxxcjb");
			attrValueMap.put(new OrganizationFullNameAttrValue().getKey(), new OrganizationFullNameAttrValue());
			attrValueMap.put(new OrganizationCodeAttrValue().getKey(), new OrganizationCodeAttrValue());
			attrValueMap.put(new LegalRepresentativeAttrValue().getKey(), new LegalRepresentativeAttrValue());
			attrValueMap.put(new UnitPropertiesAttrValue().getKey(), new UnitPropertiesAttrValue());
			attrValueMap.put(new OrganizationCategoryAttrValue().getKey(), new OrganizationCategoryAttrValue());
			
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "dzzxxcjb":
			//储存word类型
			this.attrValueContext.put("wordType", "dzzxxcjb");
			attrValueMap.put(new PartyOrganizationNameAttrValue().getKey(), new PartyOrganizationNameAttrValue());
			attrValueMap.put(new PartyCategoryAttrValue().getKey(), new PartyCategoryAttrValue());
			attrValueMap.put(new SartySecretaryAttrValue().getKey(), new SartySecretaryAttrValue());
			attrValueMap.put(new PartyContactAttrValue().getKey(), new PartyContactAttrValue());
			attrValueMap.put(new PartyContactOphAttrValue().getKey(), new PartyContactOphAttrValue());
			attrValueMap.put(new PartyContactCphAttrValue().getKey(), new PartyContactCphAttrValue());
			attrValueMap.put(new DanweiOfPartyAttrValue().getKey(), new DanweiOfPartyAttrValue());
			
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "liangwei":
			//储存word类型
			this.attrValueContext.put("wordType", "gbrmb");
			// 放入 属性 - 属性获取处理器
			attrValueMap.put(new NameAttrValue().getKey(), new NameAttrValue());
			attrValueMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValue());
			attrValueMap.put(new GenderAttrValue().getKey(), new GenderAttrValue());
			attrValueMap.put(new BirthdayAttrValue().getKey(), new BirthdayAttrValue());
			attrValueMap.put(new NationAttrValue().getKey(), new NationAttrValue());
			attrValueMap.put(new NativeAttrValue().getKey(), new NativeAttrValue());
			attrValueMap.put(new BirthPlaceAttrValue().getKey(), new BirthPlaceAttrValue());
			attrValueMap.put(new PartyDateAttrValue().getKey(), new PartyDateAttrValue());
			attrValueMap.put(new JobDateAttrValue().getKey(), new JobDateAttrValue());
			attrValueMap.put(new HealthyAttrValue().getKey(), new HealthyAttrValue());
			attrValueMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValue());
			attrValueMap.put(new SpecialtyAttrValue().getKey(), new SpecialtyAttrValue());
			attrValueMap.put(new FtDiplomaAndDegreeAttrValue().getKey(), new FtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new FtInstituteAndSpecialtyAttrValue().getKey(), new FtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PtDiplomaAndDegreeAttrValue().getKey(), new PtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new PtInstituteAndSpecialtyAttrValue().getKey(), new PtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PositionAttrValue().getKey(), new PositionAttrValue());
			attrValueMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValue());
			attrValueMap.put(new HornorAttrValue().getKey(), new HornorAttrValue());
			attrValueMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValue());
			attrValueMap.put(new FamilyRelAttrValue().getKey(), new FamilyRelAttrValue());
			attrValueMap.put(new FamilyNameAttrValue().getKey(), new FamilyNameAttrValue());
			attrValueMap.put(new FamilyAgeAttrValue().getKey(), new FamilyAgeAttrValue());
			attrValueMap.put(new FamilyPartyAttrValue().getKey(), new FamilyPartyAttrValue());
			attrValueMap.put(new FamilyOrgAndJobAttrValue().getKey(), new FamilyOrgAndJobAttrValue());
			// 放入 属性 - 属性值处理器
			attrValueProcessorMap.put(new NameAttrValue().getKey(), new NameAttrValueProcessor());
			attrValueProcessorMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValueProcessor());
			attrValueProcessorMap.put(new NationAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new NativeAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new BirthPlaceAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new SpecialtyAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new FtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
			attrValueProcessorMap.put(new PtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
//			attrValueProcessorMap.put(new PositionAttrValue().getKey(), new PositionAttrValueProcessor());
//			attrValueProcessorMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValueProcessor());
//			attrValueProcessorMap.put(new HornorAttrValue().getKey(), new HornorAttrValueProcessor());
//			attrValueProcessorMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValueProcessor());
//			attrValueProcessorMap.put(new FamilyOrgAndJobAttrValue().getKey(), new FamilyOrgAndJobAttrValueProcessor());
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		case "liangweifmid":
			//储存word类型
			this.attrValueContext.put("wordType", "gbrmb");
			// 放入 属性 - 属性获取处理器
			attrValueMap.put(new NameAttrValue().getKey(), new NameAttrValue());
			attrValueMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValue());
			attrValueMap.put(new GenderAttrValue().getKey(), new GenderAttrValue());
			attrValueMap.put(new BirthdayAttrValue().getKey(), new BirthdayAttrValue());
			attrValueMap.put(new NationAttrValue().getKey(), new NationAttrValue());
			attrValueMap.put(new NativeAttrValue().getKey(), new NativeAttrValue());
			attrValueMap.put(new BirthPlaceAttrValue().getKey(), new BirthPlaceAttrValue());
			attrValueMap.put(new PartyDateAttrValue().getKey(), new PartyDateAttrValue());
			attrValueMap.put(new JobDateAttrValue().getKey(), new JobDateAttrValue());
			attrValueMap.put(new HealthyAttrValue().getKey(), new HealthyAttrValue());
			attrValueMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValue());
			attrValueMap.put(new SpecialtyAttrValue().getKey(), new SpecialtyAttrValue());
			attrValueMap.put(new FtDiplomaAndDegreeAttrValue().getKey(), new FtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new FtInstituteAndSpecialtyAttrValue().getKey(), new FtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PtDiplomaAndDegreeAttrValue().getKey(), new PtDiplomaAndDegreeAttrValue());
			attrValueMap.put(new PtInstituteAndSpecialtyAttrValue().getKey(), new PtInstituteAndSpecialtyAttrValue());
			attrValueMap.put(new PositionAttrValue().getKey(), new PositionAttrValue());
			attrValueMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValue());
			attrValueMap.put(new HornorAttrValue().getKey(), new HornorAttrValue());
			attrValueMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValue());
			attrValueMap.put(new FamilyRelAttrValue().getKey(), new FamilyRelAttrValue());
			attrValueMap.put(new FamilyNameAttrValue().getKey(), new FamilyNameAttrValue());
			attrValueMap.put(new FamilyAgeAttrValue().getKey(), new FamilyAgeAttrValue());
			attrValueMap.put(new FamilyPartyAttrValue().getKey(), new FamilyPartyAttrValue());
			attrValueMap.put(new FamilyOrgAndJobAndIdAttrValue().getKey(), new FamilyOrgAndJobAndIdAttrValue());

			// 放入 属性 - 属性值处理器
			attrValueProcessorMap.put(new NameAttrValue().getKey(), new NameAttrValueProcessor());
			attrValueProcessorMap.put(new PhotoAttrValue().getKey(), new PhotoAttrValueProcessor());
			attrValueProcessorMap.put(new NationAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new NativeAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new BirthPlaceAttrValue().getKey(), new GenericNameAttrValueProcessor());
			attrValueProcessorMap.put(new TechnicPositionAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new SpecialtyAttrValue().getKey(), new TechnicPositionAttrValueProcessor());
			attrValueProcessorMap.put(new FtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
			attrValueProcessorMap.put(new PtDiplomaAndDegreeAttrValue().getKey(),
					new DiplomaAndDegreeAttrValueProcessor());
//			attrValueProcessorMap.put(new PositionAttrValue().getKey(), new PositionAttrValueProcessor());
//			attrValueProcessorMap.put(new ResumeAttrValue().getKey(), new ResumeAttrValueProcessor());
//			attrValueProcessorMap.put(new HornorAttrValue().getKey(), new HornorAttrValueProcessor());
//			attrValueProcessorMap.put(new EvaluationAttrValue().getKey(), new EvaluationAttrValueProcessor());
//			attrValueProcessorMap.put(new FamilyOrgAndJobAndIdAttrValue().getKey(), new FamilyOrgAndJobAttrValueProcessor());
			attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
			break;
		}

	}

	/**
	 * 根据EmpId获取所有属性的值
	 *
	 * @param empId
	 *            人员唯一编号
	 * @return 属性名和属性值的键值对
	 * @throws Exception
	 *             SQL执行异常
	 */
	public Map<String, Object> getAllAttrValue(String empId, ExportService exportWordService) throws Exception {

		Map<String, Object> attrValues = new HashMap<String, Object>(59);
		// 排序
		List<Map.Entry<String, AttrValue>> sortedList = new ArrayList<Map.Entry<String, AttrValue>>(
				attrValueMap.entrySet());
		Collections.sort(sortedList, new Comparator<Map.Entry<String, AttrValue>>() {
			@Override
			public int compare(Map.Entry<String, AttrValue> o1, Map.Entry<String, AttrValue> o2) {
				if (o1.getValue().getOrder() > o2.getValue().getOrder()) {
					return 1;
				} else if (o1.getValue().getOrder() < o2.getValue().getOrder()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		for (Map.Entry<String, AttrValue> entry : sortedList) {
			AttrValue attrRule = entry.getValue();
			try {
				Object attrValue = attrRule.getAttrValue(attrValueContext, empId, exportWordService);
			    attrValues.put(entry.getKey(), attrValue);
			}catch(AttrValueException e) {
			    logger.error("人员标识为 "+empId+" 的 "+ attrRule.getTitle()+" 数据有误--"+e.getMessage(),e);
			    throw new AttrValueException(attrRule.getTitle()+" 数据有误--"+e.getMessage(),e);
			}catch(Exception e) {
			    logger.error("人员标识为 "+empId+" 的 "+ attrRule.getTitle()+" 数据有误",e);
			    throw new AttrValueException(attrRule.getTitle()+" 数据有误",e);
			}
		}
		return attrValues;
	}

	/**
	 * 执行属性值样式处理器
	 *
	 * @param templateFilePath
	 *            模板文件路径
	 * @param attrValues
	 *            属性名和属性值的键值对
	 * @param outputFilePath
	 *            文件输出路径
	 */
	@SuppressWarnings("unchecked")
	public void processAttrValue(String templateFilePath, Map<String, Object> attrValues, String outputFilePath)
			throws Exception {

		// 根据resume的内容更换模板
		List<Map<String, Object>> resume = (List<Map<String, Object>>) attrValues.get("resume");

		List<Integer> lineWords = new ArrayList<Integer>();
		if (resume != null && resume.size() > 0) {
			for (Map<String, Object> map : resume) {
				String resumeContent = StringUtil.obj2Str(map.get("resumeContent"));
				Integer count = 0;
				// 递归判断该条断行处
				getRightLine(resumeContent);
				for (Integer addLine : addLines) {
					count += addLine == null ? 0 : addLine;
				}
				if (addLines.size() > 0) {
					count = resumeContent.length() - addLines.size() * 4;
				} else {
					count = count + resumeContent.length();
				}
				lineWords.add(count);
				addLines = new ArrayList<Integer>();
			}
		}

		GetRightFontSizeProcessor getRightFontSizeProcessor = new GetRightFontSizeProcessor();
		double rightFontSize = getRightFontSizeProcessor.getRightFontSize(lineWords);
		// 如果字体大于等于12小于等于14则使用悬挂缩进为10.5的
		Map<Double, String> fontSize2Template = new HashMap<Double, String>();
		fontSize2Template.put(12d, "_11.docx");
		fontSize2Template.put(11d, "_11.docx");
		fontSize2Template.put(10.5d, "_11.docx");
		fontSize2Template.put(10d, "_11.docx");
		fontSize2Template.put(9d, "_11_5.docx");
		fontSize2Template.put(8d, "_11_5.docx");
		fontSize2Template.put(7.5d, "_12.docx");
		fontSize2Template.put(6.5d, "_12.docx");
		String templateName = fontSize2Template.get(rightFontSize);
		if (StringUtils.isNotEmpty(templateName)) {
			if (templateFilePath.endsWith(".doc")) {
				String s = templateFilePath.replaceAll("\\.doc", templateName);
				templateFilePath = s.substring(0, s.lastIndexOf(".")) + ".doc";
			} else {
				templateFilePath = templateFilePath.replaceAll("\\.docx", templateName);
			}
		} else {
			templateFilePath = templateFilePath.replaceAll("\\.docx", "_10_5.docx");
		}
		// 获取模板得到document对象
		Path path = Paths.get(templateFilePath);
		if (Files.notExists(path)) {
			throw new RuntimeException("模版文件未找到");
		}
		// 破解Aspose代码
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream license = loader.getResourceAsStream("license.xml");// 凭证文件
		License aposeLic = new License();
		aposeLic.setLicense(license);

		license.close();
		Document document = new Document(path.toFile().getPath());

		for (Map.Entry<String, Object> entry : attrValues.entrySet()) {
			String attrName = entry.getKey();
			Object attrValue = entry.getValue();

			Map<String, Object> attrAndValue = new HashMap<String, Object>(1);
			if (attrValueProcessorMap.containsKey(attrName)) {
				AttrValueProcessor attrValueProcessor = attrValueProcessorMap.get(attrName);

				if (attrValue instanceof Map) {
					for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
						String key = entry1.getKey();
						String value = entry1.getValue();
						Map<String, Object> attrAndValueMap = new HashMap<String, Object>(1);
						attrAndValueMap.put(key, value);
						attrValueProcessor.processAttr(document, attrAndValueMap);
					}
				} else {
					if (StringUtils.equals(attrName, "resume")) {
						Map<String, Object> resumeMap = new HashMap<String, Object>(2);
						resumeMap.put("fontSize", rightFontSize);
						resumeMap.put("resumeContent", attrValue);
						attrValueProcessor.processAttr(document, resumeMap);
					} else {
						attrAndValue.put(attrName, attrValue);
						attrValueProcessor.processAttr(document, attrAndValue);
					}
				}
			} else {
				attrAndValue.put(attrName, attrValue);
				AttrValueProcessor defaultAttrValueProcessor = attrValueProcessorMap.get("default");
				if (attrValue instanceof Map) {
					for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
						String key = entry1.getKey();
						String value = entry1.getValue();
						Map<String, Object> attrAndValueMap = new HashMap<String, Object>(1);
						attrAndValueMap.put(key, value);
						defaultAttrValueProcessor.processAttr(document, attrAndValueMap);
					}
				} else {
					defaultAttrValueProcessor.processAttr(document, attrAndValue);
				}
			}
		}
		// 清空标记域，防止域没有被填充而输出域
		document.getMailMerge().deleteFields();
		document.save(outputFilePath);
	}


	/**
	 * 执行属性值样式处理器
	 *
	 * @param templateFilePath
	 *            模板文件路径
	 * @param attrValues
	 *            属性名和属性值的键值对
	 * @param outputFilePath
	 *            文件输出路径
	 */
	@SuppressWarnings("unchecked")
	public void processAttrValueAndToPDF(String templateFilePath, Map<String, Object> attrValues, String outputFilePath)
			throws Exception {

		// 根据resume的内容更换模板
		List<Map<String, Object>> resume = (List<Map<String, Object>>) attrValues.get("resume");

		List<Integer> lineWords = new ArrayList<Integer>();
		if (resume != null && resume.size() > 0) {
			for (Map<String, Object> map : resume) {
				String resumeContent = StringUtil.obj2Str(map.get("resumeContent"));
				Integer count = 0;
				// 递归判断该条断行处
				getRightLine(resumeContent);
				for (Integer addLine : addLines) {
					count += addLine == null ? 0 : addLine;
				}
				if (addLines.size() > 0) {
					count = resumeContent.length() - addLines.size() * 4;
				} else {
					count = count + resumeContent.length();
				}
				lineWords.add(count);
				addLines = new ArrayList<Integer>();
			}
		}

		GetRightFontSizeProcessor getRightFontSizeProcessor = new GetRightFontSizeProcessor();
		double rightFontSize = getRightFontSizeProcessor.getRightFontSize(lineWords);
		// 如果字体大于等于12小于等于14则使用悬挂缩进为10.5的
		Map<Double, String> fontSize2Template = new HashMap<Double, String>();
		fontSize2Template.put(12d, "_11.docx");
		fontSize2Template.put(11d, "_11.docx");
		fontSize2Template.put(10.5d, "_11.docx");
		fontSize2Template.put(10d, "_11.docx");
		fontSize2Template.put(9d, "_11_5.docx");
		fontSize2Template.put(8d, "_11_5.docx");
		fontSize2Template.put(7.5d, "_12.docx");
		fontSize2Template.put(6.5d, "_12.docx");
		String templateName = fontSize2Template.get(rightFontSize);
		if (StringUtils.isNotEmpty(templateName)) {
			if (templateFilePath.endsWith(".doc")) {
				String s = templateFilePath.replaceAll("\\.doc", templateName);
				templateFilePath = s.substring(0, s.lastIndexOf(".")) + ".doc";
			} else {
				templateFilePath = templateFilePath.replaceAll("\\.docx", templateName);
			}
		} else {
			templateFilePath = templateFilePath.replaceAll("\\.docx", "_10_5.docx");
		}
		// 获取模板得到document对象
		Path path = Paths.get(templateFilePath);
		if (Files.notExists(path)) {
			throw new RuntimeException("模版文件未找到");
		}
		// 破解Aspose代码
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream license = loader.getResourceAsStream("license.xml");// 凭证文件
		License aposeLic = new License();
		aposeLic.setLicense(license);

		license.close();
		Document document = new Document(path.toFile().getPath());

		for (Map.Entry<String, Object> entry : attrValues.entrySet()) {
			String attrName = entry.getKey();
			Object attrValue = entry.getValue();

			Map<String, Object> attrAndValue = new HashMap<String, Object>(1);
			if (attrValueProcessorMap.containsKey(attrName)) {
				AttrValueProcessor attrValueProcessor = attrValueProcessorMap.get(attrName);

				if (attrValue instanceof Map) {
					for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
						String key = entry1.getKey();
						String value = entry1.getValue();
						Map<String, Object> attrAndValueMap = new HashMap<String, Object>(1);
						attrAndValueMap.put(key, value);
						attrValueProcessor.processAttr(document, attrAndValueMap);
					}
				} else {
					if (StringUtils.equals(attrName, "resume")) {
						Map<String, Object> resumeMap = new HashMap<String, Object>(2);
						resumeMap.put("fontSize", rightFontSize);
						resumeMap.put("resumeContent", attrValue);
						attrValueProcessor.processAttr(document, resumeMap);
					} else {
						attrAndValue.put(attrName, attrValue);
						attrValueProcessor.processAttr(document, attrAndValue);
					}
				}
			} else {
				attrAndValue.put(attrName, attrValue);
				AttrValueProcessor defaultAttrValueProcessor = attrValueProcessorMap.get("default");
				if (attrValue instanceof Map) {
					for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
						String key = entry1.getKey();
						String value = entry1.getValue();
						Map<String, Object> attrAndValueMap = new HashMap<String, Object>(1);
						attrAndValueMap.put(key, value);
						defaultAttrValueProcessor.processAttr(document, attrAndValueMap);
					}
				} else {
					defaultAttrValueProcessor.processAttr(document, attrAndValue);
				}
			}
		}
		// 清空标记域，防止域没有被填充而输出域
		document.getMailMerge().deleteFields();

		//加密
		PdfSaveOptions saveOption = new PdfSaveOptions();
		saveOption.setSaveFormat(SaveFormat.PDF);
		//user pass 设置了打开时，需要密码
		//owner pass 控件编辑等权限
		PdfEncryptionDetails encryptionDetails = new PdfEncryptionDetails("123456q", "12345q", PdfEncryptionAlgorithm.RC_4_128);
		encryptionDetails.setPermissions(PdfPermissions.DISALLOW_ALL);
		saveOption.setEncryptionDetails(encryptionDetails);
		document.save(outputFilePath, saveOption);
	}

	private int getRightLine(String resumeContent) {
		Pattern compile = Pattern.compile("[：| ；]\\d{4}\\.");
		Matcher matcher = compile.matcher(resumeContent);

		Pattern pattern = Pattern.compile("\\d{4}\\.");
		Matcher matcherNumber = pattern.matcher(resumeContent);
		if (matcher.find()) {
			int start = matcher.start();
			if (start >= 20 && start < 27) {
				addLines.add(27 - start);
			}
			if (matcherNumber.find() && (start - 8) >= 20) {
				addLines.add(35 - start);
			}
			String right = resumeContent.substring(start + 1);
			int i = getRightLine(right.trim());
			if (i == -1) {
				return -1;
			}
		} else {
			return -1;
		}
		return -1;
	}
	
	public void setPhotoFilePath(String photoFilePath){
		this.attrValueContext.put("photoFilePath", photoFilePath);
	}
}
