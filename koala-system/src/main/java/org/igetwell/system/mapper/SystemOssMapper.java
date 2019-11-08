package org.igetwell.system.mapper;


import org.igetwell.common.uitls.Pagination;
import org.igetwell.system.dto.SystemOssDto;
import org.igetwell.system.entity.SystemOss;

import java.util.List;

public interface SystemOssMapper {

    SystemOss get(Long id);

    List<SystemOss> pageList(Pagination pagination, SystemOssDto oss);

    int deleteById(Long id);

    int insert(SystemOss oss);

    int update(SystemOss oss);
}