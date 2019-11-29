package org.igetwell.system.service;

import org.igetwell.common.uitls.Pagination;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.dto.SystemOssDto;
import org.igetwell.system.entity.SystemOss;

import java.util.List;

public interface ISystemOssService {

    SystemOss get(Long id);

    List<SystemOss> getList(Pagination pagination, SystemOssDto oss);

    void deleteById(Long id);

    ResponseEntity insert(SystemOss oss);

    ResponseEntity update(SystemOss oss);
}
