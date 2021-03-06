package org.ovirt.engine.core.bll.scheduling.external;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.ovirt.engine.core.common.utils.Pair;
import org.ovirt.engine.core.compat.Guid;

public interface ExternalSchedulerBroker {
    Optional<ExternalSchedulerDiscoveryResult> runDiscover();

    @NotNull
    List<Guid> runFilters(@NotNull List<String> filterNames,
            @NotNull List<Guid> hostIDs, @NotNull Guid vmID, @NotNull Map<String, String> propertiesMap);

    @NotNull
    List<WeightResultEntry> runScores(@NotNull List<Pair<String, Integer>> scoreNameAndWeight,
            @NotNull List<Guid> hostIDs,
            @NotNull Guid vmID,
            @NotNull Map<String, String> propertiesMap);

    Optional<BalanceResult> runBalance(String balanceName, List<Guid> hostIDs, Map<String, String> propertiesMap);

}
