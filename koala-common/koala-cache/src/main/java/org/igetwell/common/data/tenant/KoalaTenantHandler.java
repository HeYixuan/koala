package org.igetwell.common.data.tenant;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * 租户维护处理器
 */
@Slf4j
public class KoalaTenantHandler implements TenantHandler {
	@Autowired
	private KoalaTenantConfigProperties properties;

	/**
	 * 获取租户值
	 * <p>
	 * TODO 校验租户状态
	 *
	 * @return 租户值
	 */
	@Override
	public Expression getTenantId() {
		String tenantId = TenantContextHolder.getTenantId();
		log.debug("当前租户为 >> {}", tenantId);

		if (StringUtils.isEmpty(tenantId)) {
			return new NullValue();
		}
		return new LongValue(tenantId);
	}

	/**
	 * 获取租户字段名
	 *
	 * @return 租户字段名
	 */
	@Override
	public String getTenantIdColumn() {
		return properties.getColumn();
	}

	/**
	 * 根据表名判断是否进行过滤
	 *
	 * @param tableName 表名
	 * @return 是否进行过滤
	 */
	@Override
	public boolean doTableFilter(String tableName) {
		String tenantId = TenantContextHolder.getTenantId();
		// 租户中ID 为空，查询全部，不进行过滤
		if (StringUtils.isEmpty(tenantId)) {
			return Boolean.TRUE;
		}

		return !properties.getTables().contains(tableName);
	}
}
