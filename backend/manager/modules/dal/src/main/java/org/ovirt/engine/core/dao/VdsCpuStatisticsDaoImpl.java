package org.ovirt.engine.core.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ovirt.engine.core.common.businessentities.CpuStatistics;
import org.ovirt.engine.core.compat.Guid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Named
@Singleton
public class VdsCpuStatisticsDaoImpl extends BaseDao implements VdsCpuStatisticsDao {

    @Override
    public List<CpuStatistics> getAllCpuStatisticsByVdsId(Guid vdsId) {
        return getCallsHandler().executeReadList("GetVdsCpuStatisticsByVdsId",
                cpuStatisticsRowMapper,
                getCustomMapSqlParameterSource().addValue("vds_id", vdsId));
    }

    @Override
    public void massSaveCpuStatistics(List<CpuStatistics> vdsCpuStatistics, Guid vdsId) {
        List<MapSqlParameterSource> executions = vdsCpuStatistics.stream()
                .map(stats -> createCpuStatisticsParametersMapper(stats)
                    .addValue("vds_id", vdsId)
                    .addValue("vds_cpu_id", Guid.newGuid()))
                .collect(Collectors.toList());

        getCallsHandler().executeStoredProcAsBatch("InsertVdsCpuStatistics", executions);
    }

    @Override
    public void massUpdateCpuStatistics(List<CpuStatistics> vdsCpuStatistics, Guid vdsId) {
        List<MapSqlParameterSource> executions = vdsCpuStatistics.stream()
                .map(stats -> createCpuStatisticsParametersMapper(stats).addValue("vds_id", vdsId))
                .collect(Collectors.toList());

        getCallsHandler().executeStoredProcAsBatch("UpdateVdsCpuStatistics", executions);
    }

    @Override
    public void removeAllCpuStatisticsByVdsId(Guid vdsId) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vds_id", vdsId);

        getCallsHandler().executeModification("DeleteVdsCpuStatisticsByVdsId", parameterSource);
    }

    private MapSqlParameterSource createCpuStatisticsParametersMapper(CpuStatistics stat) {
        return getCustomMapSqlParameterSource()
                .addValue("cpu_core_id", stat.getCpuId())
                .addValue("cpu_sys", stat.getCpuSys())
                .addValue("cpu_user", stat.getCpuUser())
                .addValue("cpu_idle", stat.getCpuIdle())
                .addValue("usage_cpu_percent", stat.getCpuUsagePercent());
    }

    private static final RowMapper<CpuStatistics> cpuStatisticsRowMapper = (rs, rowNum) -> {
        CpuStatistics entity = new CpuStatistics();
        entity.setCpuId(rs.getInt("cpu_core_id"));
        entity.setCpuSys(rs.getDouble("cpu_sys"));
        entity.setCpuUser(rs.getDouble("cpu_user"));
        entity.setCpuIdle(rs.getDouble("cpu_idle"));
        entity.setCpuUsagePercent(rs.getInt("usage_cpu_percent"));
        return entity;
    };
}
