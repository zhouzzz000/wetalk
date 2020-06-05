package com.zhou.wetalk;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/12
 * @Time 13:46
 * @ClassName FastDFSImporter
 * @see
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDFSImporter {
    // 导入依赖组件
}