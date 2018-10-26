//package com.prototype.microservice.cms.config;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import com.prototype.microservice.cms.batch.processor.CmsRowProcessor;
//import com.prototype.microservice.cms.repository.CmsBlogRepository;
//import com.prototype.microservice.cms.repository.CmsCategoryRepository;
//import com.prototype.microservice.cms.repository.CmsLabelRepository;
//import com.prototype.microservice.cms.repository.CmsRefRepository;
//import com.prototype.microservice.cms.utils.CmsMessageHelper;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.launch.support.SimpleJobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.data.RepositoryItemWriter;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.prototype.microservice.cms.batch.mapper.CmsRowMapper;
//import com.prototype.microservice.cms.batch.mapper.PoiRowMapper;
//import com.prototype.microservice.cms.batch.processor.BatchReporter;
//import com.prototype.microservice.cms.batch.processor.CmsEntityDayendCopyProcessor;
//import com.prototype.microservice.cms.batch.processor.CmsItemProcessor;
//import com.prototype.microservice.cms.batch.reader.CmsItemReader;
//import com.prototype.microservice.cms.converter.CmsStockEntityDtoConverter;
//import com.prototype.microservice.cms.dto.CmsStockDto;
//import com.prototype.microservice.cms.entity.CmsEntity;
//import com.prototype.microservice.cms.entity.CmsExcelRow;
//import com.prototype.microservice.cms.entity.CmsStock;
//import com.prototype.microservice.cms.entity.CmsHold;
//import com.prototype.microservice.cms.utils.CmsHelper;
//import com.prototype.microservice.cms.utils.StockConsolidatorForTrs;
//import com.prototype.microservice.cms.utils.StockHelper;
//import com.prototype.microservice.cms.repository.CmsEntityReportRepository;
//import com.prototype.microservice.cms.repository.CmsEntityRepository;
//import com.prototype.microservice.cms.repository.CmsOptionRefRepository;
//import com.prototype.microservice.cms.repository.CmsRawEntityMapRepository;
//import com.prototype.microservice.cms.repository.CmsStockRepository;
//import com.prototype.microservice.cms.service.CmsHoldService;
//import com.prototype.microservice.cms.service.CmsStockService;
//import com.prototype.microservice.cms.repository.CmsHoldRepository;
//
//
//@Configuration
//@EnableBatchProcessing
//@EnableTransactionManagement(proxyTargetClass = true)
//public class BatchConfig {
//	private final static Logger LOG = LoggerFactory.getLogger(BatchConfig.class);
//	private CmsHelper cmsHelper = CmsHelper.getInstance();
//
//	@Autowired
//	DataSource dataSource;
//	@Autowired
//	private Environment env;
//	JobExecution tradeJobExecution;
//	@Autowired
//	private SimpleJobLauncher jobLauncher;
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//	private JobParameters jobParameters;
//	@Autowired
//	private CmsBlogRepository cmsBlogRepository;
//	@Autowired
//	private CmsLabelRepository cmsLabelRepository;
//	@Autowired
//	private CmsCategoryRepository cmsCategoryRepository;
//	@Autowired
//	private CmsRefRepository cmsRefRepository;
//	@Autowired
//	private JobLauncher asyncJobLauncher;
//	@Autowired
//	@Qualifier("cmsHoldService")
//	private CmsHoldService cmsHoldService;
//	@Autowired
//	@Qualifier("cmsStockService")
//	private CmsStockService cmsStockService;
//	@Autowired
//	private EntityManagerFactory entityManagerFactory;
//	@Autowired
//	CmsRowProcessor cmsRowProcessor;
//	@Autowired
//	CmsMessageHelper msgHelper;
//
//	private List<CmsEntity> cmsEntityList;
//	private Map<String, CmsStockDto> stockDeptMap;
//	private Map<String, List<CmsHold>> stockHoldMap;
//
//	private static LocalDate tradeDateForEntity;
//	private static LocalDate tradeDateForTradeJob;
//	private static LocalDate tradeDateForCalcRpt;
//	private File cmsFile;
//
//	@Bean
//	@JobScope
//	CmsRowProcessor cmsExcelItemProcessor() {
////		cmsItemProcessor.setCmsEntityList(cmsEntityList);
////		cmsItemProcessor.setFileModifiedDatetime(CmsHelper.getFileLastModified(cmsFile));
////		stockDeptMap = new HashMap<String, CmsStockDto>();
////		stockHoldMap = new HashMap<String, List<CmsHold>>();
////		cmsItemProcessor.setStockDeptMap(stockDeptMap);
////		cmsItemProcessor.setStockHoldMap(stockHoldMap);
//		return cmsRowProcessor;
//	}
//
//	@Bean
//	@JobScope
//	ItemReader<CmsExcelRow> excelCmsReader() throws Exception {
//		CmsItemReader<CmsExcelRow> reader = new CmsItemReader<>();
//		if(cmsFile!=null){
//			FileSystemResource fileResource = new FileSystemResource(cmsFile);
//			LOG.info("===========================>"+cmsFile.getCanonicalPath());
//			reader.setResource(fileResource);
//			reader.setRowsToSkip(2);
//			reader.setRowMapper(excelRowMapper());
//			reader.setTitleRowIndex(Integer.parseInt(env.getProperty("cms-service.batch.file.titleRowIndex")));
//			reader.setDescnRowIndex(Integer.parseInt(env.getProperty("cms-service.batch.file.descnRowIndex")));
//			reader.setDescnColIndex(Integer.parseInt(env.getProperty("cms-service.batch.file.descnColIndex")));
//		}
//		return reader;
//	}
//
//	private PoiRowMapper<CmsExcelRow> excelRowMapper() {
//		return new CmsRowMapper();
//	}
//
//	@Bean
//	public JpaItemWriter<CmsHold> jpaItemWriter() {
//		JpaItemWriter<CmsHold> writer = new JpaItemWriter<>();
//		writer.setEntityManagerFactory(entityManagerFactory);
//		return writer;
//	}
//
//	@Bean
//	public RepositoryItemWriter<CmsHold> repositoryWriter() {
//		RepositoryItemWriter<CmsHold> writer = new RepositoryItemWriter<>();
//		writer.setRepository(cmsHoldRepository);
//		writer.setMethodName("save");
//		return writer;
//	}
//
//	@Bean
//	@JobScope
//	public Step stepCopyStockInfo() {
//		return stepBuilderFactory.get("stepCopyStockInfo").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				try {
//					LocalDate lastTradeDate = cmsHoldRepository.findLastTradeDate(tradeDateForTradeJob);
//					cmsEntityReportRepository.deleteByDate(tradeDateForTradeJob);
//					deleteStockTradeByData(tradeDateForTradeJob);
//					//cmsStockRepository.copyAsTradeDate(lastTradeDate, tradeDateForTradeJob, false);
//					cmsStockRepository.copyStockDataByTradeDate(lastTradeDate, tradeDateForTradeJob, false);
//					//cmsStockRepository.setPreviousDiscPctByTradeDate(tradeDateForTradeJob);
//				} catch (Exception e) {
//					e.printStackTrace();
//					return RepeatStatus.FINISHED;
//				}
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//
//	@Bean
//	@JobScope
//	public Step stepCalcReportable() {
//		return stepBuilderFactory.get("stepCalcReportable").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				try {
//					//cmsEntityReportRepository.deleteByDate(tradeDateForCalcRpt);
//					cmsHoldService.analyseReportable(tradeDateForCalcRpt);
//					//calcReportableByDate(tradeDateForCalcRpt);
//				} catch (Exception e) {
//					e.printStackTrace();
//					return RepeatStatus.FINISHED;
//				}
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//
//	@Bean
//	@JobScope
//	public Step stepGetEntityList() {
//		return stepBuilderFactory.get("stepGetEntityList").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				cmsEntityList = cmsBuzEntityRepo.findAllByTradeDate(tradeDateForTradeJob);
//				if(cmsEntityList==null||cmsEntityList.size()<=0){
//					LOG.error("Cannot find entities on "+ tradeDateForTradeJob.toString());
//					throw new RuntimeException("Error: Cannot find entities on "+tradeDateForTradeJob.toString());
//				}
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//
//	@Bean
//	@JobScope
//	public Step stepExtractCmsTradeData() throws Exception {
//		return stepBuilderFactory.get("stepExtractCmsTradeData").<CmsExcelRow, CmsHold> chunk(10)
//				.reader(excelCmsReader()).processor(cmsTradeExcelItemProcessor()).writer(repositoryWriter()).build();
//	}
//
//	@Bean
//	@JobScope
//	public Step stepConsolidateCmsStock() {
//		return stepBuilderFactory.get("stepConsolidateCmsStock").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				//CmsItemProcessor.cleanStockCodeNameMapper();
//				if(stockDeptMap==null||stockDeptMap.isEmpty()){
//					return RepeatStatus.FINISHED;
//				}
//				// update stock info
//				for (Entry<String, CmsStockDto> entry : stockDeptMap.entrySet()) {
//					CmsStockDto newStockDto = entry.getValue();
//					if (newStockDto != null && newStockDto.getUnderlySecSyb() != null && newStockDto.getDepartment() != null
//							&& newStockDto.getTradeDate() != null) {
//						//StockCommonVisibleInfo info = CmsItemProcessor.getStockCommonInfo(stock.getUnderlySecSyb());
//						String stockHoldMapKey = newStockDto.getUnderlySecSyb();
//						if(stockHoldMap!=null&&stockHoldMap.size()>0&&stockHoldMap.containsKey(stockHoldMapKey)){
//							String[] stockCodeName = StockHelper.getStockCodeNameFromHoldList(stockHoldMap.get(stockHoldMapKey));
//							BigDecimal totalIssuedShares = StockHelper.getTotalIssuedSharesFromHoldList(stockHoldMap.get(stockHoldMapKey));
//							totalIssuedShares = (totalIssuedShares == null ? BigDecimal.ZERO : totalIssuedShares);
//							newStockDto.setTotalIssuedShares(totalIssuedShares);
//							if (stockCodeName != null && stockCodeName.length == 2) {
//								newStockDto.setStockCode(stockCodeName[0]);
//								newStockDto.setStockName(stockCodeName[1]);
//							}
//						}
////						String[] stockCodeName = cmsHoldRepository.findStockCodeAndName(stock.getTradeDate(), stock.getUnderlySecSyb(), Arrays.asList(CmsHelper.INVALID_STRING));
////						BigDecimal totalIssuedShares = cmsHoldRepository.findTotalIssuedShares(stock.getTradeDate(), stock.getUnderlySecSyb());
//					}
//					CmsStock stock = stockDtoConverter.convertDtoToEntity(newStockDto);
//					int upNum = cmsStockRepository.updateByDateSecDept(stock);
//					if(upNum==0){
//						cmsStockRepository.save(stock);
//					}
//				}
//				//special handling for ED TRS
//				cmsStockService.handleLongShortForEDTrs(CmsHelper.formatLocalDate(tradeDateForTradeJob));
//
//				// T-1 has holding but T has no holding
//				LocalDate prevDate = cmsHoldRepository.findLastTradeDate(tradeDateForTradeJob);
//				if(prevDate!=null){
//					List<String[]> info = cmsHoldRepository.findCurrentEmptyPositionDepartment(prevDate, tradeDateForTradeJob);
//					for (String[] item : info) {
//						String underlySecSyb = item[0];
//						String dept = item[1];
//
//						CmsStock stock = cmsStockRepository.findBySecDateDepartment(underlySecSyb,dept,tradeDateForTradeJob);
//						if(stock!=null){
//							stock.setSharesNumLong(BigDecimal.ZERO);
//							stock.setSharesNumShort(BigDecimal.ZERO);
//							stock.setIsDisc3Pct(false);
//							stock.setIsDisc5Pct(false);
//							cmsStockRepository.update(stock);
//						}
//					}
//				}
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//	@Bean
//	@JobScope
//	public Step stepCleanTradeJob(){
//		return stepBuilderFactory.get("stepCleanTradeJob").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				cleanTradeJobParam();
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//	public void cleanTradeJobParam(){
//		tradeDateForTradeJob = null;
//		cmsFile = null;
//		jobParameters=null;
//		if(cmsEntityList!=null){
//			cmsEntityList.clear();
//			cmsEntityList = null;
//		}
//		if(stockDeptMap!=null){
//			stockDeptMap.clear();
//			stockDeptMap = null;
//		}
//		if(stockHoldMap!=null){
//			stockHoldMap.clear();
//			stockHoldMap = null;
//		}
//	}
//	@Bean
//	@JobScope
//	public Step stepAnalyseReportableInTradeJob() {
//		return stepBuilderFactory.get("stepAnalyseReportableInTradeJob").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				cmsHoldService.analyseReportableForScheduledJob(stockHoldMap, tradeDateForTradeJob);
//				return RepeatStatus.FINISHED;
//			}}).build();
//	}
////	public void calcReportableByDate(LocalDate tradeDate){
////		List<String[]> secEntityKeyInfo = cmsHoldRepository.findByDistinctDateSecEntity(tradeDate);
////		// delete the record first if exist
////		cmsEntityReportRepository.deleteByDate(tradeDate);
////		if (secEntityKeyInfo != null && secEntityKeyInfo.size() > 0) {
////			for (String[] info : secEntityKeyInfo) {
////				String underlySecSy = info[1];
////				//String entityKey = info[2];
////				LOG.info("Calculate reportable:"+tradeDate.toString()+" "+underlySecSy);
////				cmsHoldService.analyseReportable(tradeDate, underlySecSy);
////				//batchReporter.analyse(tradeDate, underlySecSy, entityKey);
////			}
////		}
////		batchReporter.analyseCurPosnIsEmpty(tradeDate);
////	}
//	@Bean
//	public Job tradeJob() throws Exception {
//		return jobBuilderFactory.get("tradeJob").preventRestart().incrementer(new RunIdIncrementer()).start(stepGetEntityList()).next(stepCopyStockInfo())
//				.next(stepExtractCmsTradeData()).next(stepConsolidateCmsStock()).next(stepAnalyseReportableInTradeJob()).next(stepCleanTradeJob()).build();
//	}
//	@Bean
//	public Job stockJob() throws Exception {
//		return jobBuilderFactory.get("stockJob").preventRestart().incrementer(new RunIdIncrementer()).start(stepCopyStockInfo()).build();
//	}
//	@Bean
//	public Job calcReportabelJob() throws Exception{
//		return jobBuilderFactory.get("calcReportabelJob").preventRestart().incrementer(new RunIdIncrementer()).start(stepCalcReportable()).build();
//	}
////
////	@Scheduled(cron = "${cms-service.batch.schedule.stock-job}")
////	public void runStockCopyJob() {
////		copyStockInfoFromLastTradeDate(null, false);
////	}
//	public void deleteStockTradeByData(LocalDate tradeDate){
//		try{
//			cmsStockRepository.deleteByDate(tradeDate);
//			cmsHoldRepository.deleteByDate(tradeDate);
//		}catch(Exception e){
//			LOG.error(e.getMessage());
//		}
//
//	}
//
//	public String calcReportable(String[] args, boolean isAsync){
//		tradeDateForCalcRpt = cmsHelper.getTradeDate();
//		String jobId="cms-RPT-";
//		String paramKey="CalcRepotableJobTradeDate";
//		JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
//		if(args != null && args.length >= 2 && StringUtils.isNotBlank(args[0]) && StringUtils.isNotBlank(args[1])){
//			tradeDateForCalcRpt = CmsHelper.parseIsoDate(args[0]);
//			jobId+="MAN-RUN-"+args[1];
//			jobParameterBuilder.addString(paramKey+args[1], tradeDateForCalcRpt.toString());
//			LOG.info("Cms Reportable Job Started at :" + LocalDate.now().toString());
//			//calcReportableByDate(tradeDateForCalcRpt);
//		}else{
//			return null;
//		}
//		JobExecution execution = null;
//		try{
//			jobParameterBuilder.addString("JobID", jobId);
//			jobParameters = jobParameterBuilder.toJobParameters();
//			if(isAsync){
//				execution = asyncJobLauncher.run(calcReportabelJob(), jobParameters);
//				return execution.getId().toString();
//			}else{
//				execution = jobLauncher.run(calcReportabelJob(), jobParameters);
//			}
//			LOG.info("Cms calculate reportable Job finished with status :" + execution.getStatus());
//		}catch(Exception e){
//			execution.stop();
//			LOG.error(e.getMessage());
//		}
//		return null;
//	}
//	@Scheduled(cron = "${cms-service.batch.schedule.trade-job}")
//	public void runTradeJob() throws Exception {
//		extractTradeInfo(null,false);
//	}
//
//	public String runTradeJobAsync(String[] args) throws Exception{
//		return extractTradeInfo(args,true);
//	}
//	public String runCalcRptJobAsync(String[] args){
//		return calcReportable(args, true);
//	}
//
//	public String extractTradeInfo(String[] args, boolean isAsync) throws Exception{
//		if(tradeJobExecution!=null&&tradeJobExecution.isRunning()){
//			String msg = msgHelper.getMessage("cms-BAT-ERR-003", new Object[]{tradeDateForTradeJob.toString()});
//			LOG.error(msg);
//			throw new RuntimeException(msg);
//		}
//		String jobId="cms-HOLD-";
//		String paramKey="TradeJobTradeDate";
//
//		try{
//			LocalDate now = LocalDate.now();
//			JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
//			LOG.info("Cms Trade Job Started at :" + now.toString());
//
//			if(args != null && args.length >= 2 && StringUtils.isNotBlank(args[0]) && StringUtils.isNotBlank(args[1])){
//				tradeDateForTradeJob = CmsHelper.parseIsoDate(args[0]);
//				cmsFile = cmsHelper.getFile(tradeDateForTradeJob);
//				jobId+="MAN-RUN-"+args[1];
//				jobParameterBuilder.addString(paramKey, tradeDateForTradeJob.toString());
//			}else{
//				tradeDateForTradeJob = cmsHelper.getTradeDate();
//				cmsFile = cmsHelper.getFile(tradeDateForTradeJob);
//				if(cmsFile!=null){
//					jobId+="SCHE-RUN-"+CmsHelper.getCurrentDateTimeStr();
//					jobParameterBuilder.addString(paramKey, tradeDateForTradeJob.toString());
//				}
//			}
//			//Check file whether exist and run Trade job...
//			if(cmsFile==null){
//				String msg = msgHelper.getMessage("cms-BAT-ERR-004",new Object[]{tradeDateForTradeJob.toString()});
//				LOG.info(msg);
//				throw new RuntimeException(msg);
//			}
//			//check entity if exist
//			long entityNum = cmsBuzEntityRepo.countEntityByTradeDate(tradeDateForTradeJob);
//			if(entityNum==0){
//				String msg = msgHelper.getMessage("cms-BAT-ERR-005",new Object[]{tradeDateForTradeJob.toString()});
//				LOG.info(msg);
//				throw new RuntimeException(msg);
//			}
//			LOG.info("Cms Trade Job Trade Date:" + tradeDateForTradeJob.toString());
//			jobParameterBuilder.addString("JobID", jobId);
//			jobParameters = jobParameterBuilder.toJobParameters();
//		}catch(Exception e){
//			if (tradeJobExecution != null) {
//				tradeJobExecution.stop();
//			}
//			cleanTradeJobParam();
//			LOG.error(e.getMessage());
//			throw e;
//		}
//
//		try {
//			if (isAsync) {
//				tradeJobExecution = asyncJobLauncher.run(tradeJob(), jobParameters);
//				LOG.info("Cms Trade Job async run with job id :" + tradeJobExecution.getId().toString());
//				return tradeJobExecution.getId().toString();
//			} else {
//				if(args==null){
//					LocalDateTime latestModifiedDatetime = cmsHoldRepository.getLatestModifiedDatetime(tradeDateForTradeJob);
//					LocalDateTime fileModified = CmsHelper.getFileLastModified(cmsFile);
//					if((latestModifiedDatetime==null&&cmsFile.exists())||(fileModified!=null&&latestModifiedDatetime!=null&&fileModified.isAfter(latestModifiedDatetime))){
//						tradeJobExecution = jobLauncher.run(tradeJob(), jobParameters);
//						LOG.info("Cms Trade Job (schedule) run with status :" + tradeJobExecution.getStatus());
//					}
//				}
//			}
//		} catch (Exception e) {
//			if (tradeJobExecution != null) {
//				tradeJobExecution.stop();
//			}
//			cleanTradeJobParam();
//			LOG.error(e.getMessage());
//			throw e;
//		}
//		return null;
//	}
//	// System generate Entity data
//
//	@Scheduled(cron = "${cms-service.batch.schedule.entity-job}")
//	public void runDuplicateEntityJob() throws Exception {
//
//		runDuplicateEntityJob(null);
//
//	}
//
//	/*
//	 *  This method can be called by REST and take a specific argsList
//	 *
//	 *  argsList : {
//	 *  	"2018-01-19T00:00:00.000+0800", 	// Trade Date
//	 *  	"1522145590262"						// Unique id for the job , e.g. currentTimeMillis
//	 *  }
//	 */
//
//	public String runDuplicateEntityJob(String[] argsList) throws Exception {
//		tradeDateForEntity = null;
//		String jobId = "DE-";
//
//		if(argsList == null){
//			// Scheduled Run
//			tradeDateForEntity = cmsHelper.getSystemDate();
//			jobId = jobId + "SCHE-RUN-" + tradeDateForEntity.toString();
//		}else{
//			// Manual Run
//			if(argsList.length < 2 || StringUtils.isBlank(argsList[0]) || StringUtils.isBlank(argsList[1])){
//				return "Not enough args";
//			}
//
//			if(CmsHelper.parseIsoDate(argsList[0]) == null){
//				return "Trade date format error";
//			}
//
//			tradeDateForEntity = CmsHelper.parseIsoDate(argsList[0]);
//			jobId = jobId + "MAN-RUN-" + argsList[1];
//		}
//
//
//		// throws JobExecutionAlreadyRunningException when another job is running
//		//checkConcurrentJobRun("duplicateEntityJob");
//
//		LOG.info("runDuplicateEntityJob Started at :" + LocalDate.now().toString());
//		JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
//		jobParameterBuilder.addString("JobID", jobId);
//		jobParameterBuilder.addString("TradeDate", tradeDateForEntity.toString());
//		jobParameters = jobParameterBuilder.toJobParameters();
//
//		JobExecution execution = jobLauncher.run(duplicateEntityJob(), jobParameters);
//		LOG.info("runDuplicateEntityJob finished with status :" + execution.getStatus());
//
//		return execution.getStatus().toString();
//	}
//
//	@Bean
//	public Job duplicateEntityJob() throws Exception {
//		return jobBuilderFactory.get("duplicateEntityJob").preventRestart().incrementer(new RunIdIncrementer()).start(doDuplicateEntity()).build();
//	}
//
//	@Bean
//	public Step doDuplicateEntity() {
//		return stepBuilderFactory.get("doDuplicateEntity").tasklet(new Tasklet() {
//
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//
//				CmsEntityDayendCopyProcessor copyProc = new CmsEntityDayendCopyProcessor(cmsBuzEntityRepo);
////				try {
//					copyProc.process(tradeDateForEntity);
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
//
//				return RepeatStatus.FINISHED;
//			}
//		}).build();
//	}
//
//	@Bean("asyncJobLauncher")
//	public JobLauncher asyncJobLauncher(JobRepository jobRepository) {
//
//		SimpleJobLauncher launcher = new SimpleJobLauncher();
//		launcher.setJobRepository(jobRepository);
//		launcher.setTaskExecutor(threadPoolTaskExecutor());
//		return launcher;
//	}
//
//	@Bean
//	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
//		SimpleJobLauncher launcher = new SimpleJobLauncher();
//		launcher.setJobRepository(jobRepository);
//		return launcher;
//	}
//
//	@Bean("threadPoolTaskExecutor")
//	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
//		ThreadPoolTaskExecutor tExec = new ThreadPoolTaskExecutor();
//		tExec.setCorePoolSize(1);
//		tExec.setMaxPoolSize(1);
//		tExec.setWaitForTasksToCompleteOnShutdown(false);
//		return tExec;
//	}
//
//}
