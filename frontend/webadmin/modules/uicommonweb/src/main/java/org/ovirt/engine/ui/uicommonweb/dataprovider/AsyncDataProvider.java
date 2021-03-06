package org.ovirt.engine.ui.uicommonweb.dataprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ovirt.engine.core.aaa.ProfileEntry;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.EventNotificationEntity;
import org.ovirt.engine.core.common.VdcActionUtils;
import org.ovirt.engine.core.common.VdcEventNotificationUtils;
import org.ovirt.engine.core.common.VdcObjectType;
import org.ovirt.engine.core.common.action.VdcActionType;
import org.ovirt.engine.core.common.action.VmManagementParametersBase;
import org.ovirt.engine.core.common.action.gluster.GlusterVolumeRemoveBricksQueriesParameters;
import org.ovirt.engine.core.common.businessentities.ActionGroup;
import org.ovirt.engine.core.common.businessentities.AdditionalFeature;
import org.ovirt.engine.core.common.businessentities.ArchitectureType;
import org.ovirt.engine.core.common.businessentities.BusinessEntity;
import org.ovirt.engine.core.common.businessentities.CertificateInfo;
import org.ovirt.engine.core.common.businessentities.Cluster;
import org.ovirt.engine.core.common.businessentities.ClusterEditWarnings;
import org.ovirt.engine.core.common.businessentities.DisplayType;
import org.ovirt.engine.core.common.businessentities.ExternalComputeResource;
import org.ovirt.engine.core.common.businessentities.ExternalDiscoveredHost;
import org.ovirt.engine.core.common.businessentities.ExternalHostGroup;
import org.ovirt.engine.core.common.businessentities.GraphicsType;
import org.ovirt.engine.core.common.businessentities.HostDeviceView;
import org.ovirt.engine.core.common.businessentities.IVdcQueryable;
import org.ovirt.engine.core.common.businessentities.Label;
import org.ovirt.engine.core.common.businessentities.Nameable;
import org.ovirt.engine.core.common.businessentities.NumaTuneMode;
import org.ovirt.engine.core.common.businessentities.OriginType;
import org.ovirt.engine.core.common.businessentities.Permission;
import org.ovirt.engine.core.common.businessentities.Provider;
import org.ovirt.engine.core.common.businessentities.ProviderType;
import org.ovirt.engine.core.common.businessentities.Quota;
import org.ovirt.engine.core.common.businessentities.QuotaEnforcementTypeEnum;
import org.ovirt.engine.core.common.businessentities.Role;
import org.ovirt.engine.core.common.businessentities.ServerCpu;
import org.ovirt.engine.core.common.businessentities.Snapshot;
import org.ovirt.engine.core.common.businessentities.StorageDomain;
import org.ovirt.engine.core.common.businessentities.StorageDomainStatic;
import org.ovirt.engine.core.common.businessentities.StorageDomainType;
import org.ovirt.engine.core.common.businessentities.StoragePool;
import org.ovirt.engine.core.common.businessentities.StorageServerConnections;
import org.ovirt.engine.core.common.businessentities.SupportedAdditionalClusterFeature;
import org.ovirt.engine.core.common.businessentities.Tags;
import org.ovirt.engine.core.common.businessentities.TagsType;
import org.ovirt.engine.core.common.businessentities.VDS;
import org.ovirt.engine.core.common.businessentities.VDSStatus;
import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.businessentities.VdsNumaNode;
import org.ovirt.engine.core.common.businessentities.VmGuestAgentInterface;
import org.ovirt.engine.core.common.businessentities.VmHostDevice;
import org.ovirt.engine.core.common.businessentities.VmPool;
import org.ovirt.engine.core.common.businessentities.VmTemplate;
import org.ovirt.engine.core.common.businessentities.VmTemplateStatus;
import org.ovirt.engine.core.common.businessentities.aaa.AuthzGroup;
import org.ovirt.engine.core.common.businessentities.aaa.DbGroup;
import org.ovirt.engine.core.common.businessentities.aaa.DbUser;
import org.ovirt.engine.core.common.businessentities.comparators.LexoNumericComparator;
import org.ovirt.engine.core.common.businessentities.comparators.NameableComparator;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterBrickEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterClusterService;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterGeoRepNonEligibilityReason;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterGeoRepSession;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterHookEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterServerService;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeAdvancedDetails;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeSnapshotEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeSnapshotSchedule;
import org.ovirt.engine.core.common.businessentities.gluster.ServiceType;
import org.ovirt.engine.core.common.businessentities.gluster.StorageDevice;
import org.ovirt.engine.core.common.businessentities.network.BondMode;
import org.ovirt.engine.core.common.businessentities.network.HostNetworkQos;
import org.ovirt.engine.core.common.businessentities.network.Network;
import org.ovirt.engine.core.common.businessentities.network.NetworkQoS;
import org.ovirt.engine.core.common.businessentities.network.VmInterfaceType;
import org.ovirt.engine.core.common.businessentities.network.VmNetworkInterface;
import org.ovirt.engine.core.common.businessentities.network.VnicProfileView;
import org.ovirt.engine.core.common.businessentities.qos.QosType;
import org.ovirt.engine.core.common.businessentities.storage.CinderVolumeType;
import org.ovirt.engine.core.common.businessentities.storage.Disk;
import org.ovirt.engine.core.common.businessentities.storage.DiskImage;
import org.ovirt.engine.core.common.businessentities.storage.DiskInterface;
import org.ovirt.engine.core.common.businessentities.storage.ImageFileType;
import org.ovirt.engine.core.common.businessentities.storage.LUNs;
import org.ovirt.engine.core.common.businessentities.storage.LibvirtSecretUsageType;
import org.ovirt.engine.core.common.businessentities.storage.RepoImage;
import org.ovirt.engine.core.common.businessentities.storage.StorageType;
import org.ovirt.engine.core.common.businessentities.storage.VolumeFormat;
import org.ovirt.engine.core.common.businessentities.storage.VolumeType;
import org.ovirt.engine.core.common.console.ConsoleOptions.WanColorDepth;
import org.ovirt.engine.core.common.console.ConsoleOptions.WanDisableEffects;
import org.ovirt.engine.core.common.interfaces.SearchType;
import org.ovirt.engine.core.common.migration.MigrationPolicy;
import org.ovirt.engine.core.common.migration.NoMigrationPolicy;
import org.ovirt.engine.core.common.mode.ApplicationMode;
import org.ovirt.engine.core.common.queries.ArchCapabilitiesParameters;
import org.ovirt.engine.core.common.queries.ArchCapabilitiesParameters.ArchCapabilitiesVerb;
import org.ovirt.engine.core.common.queries.ClusterEditParameters;
import org.ovirt.engine.core.common.queries.ConfigurationValues;
import org.ovirt.engine.core.common.queries.GetAgentFenceOptionsQueryParameters;
import org.ovirt.engine.core.common.queries.GetAllAttachableDisksForVmQueryParameters;
import org.ovirt.engine.core.common.queries.GetAllFromExportDomainQueryParameters;
import org.ovirt.engine.core.common.queries.GetAllProvidersParameters;
import org.ovirt.engine.core.common.queries.GetAllServerCpuListParameters;
import org.ovirt.engine.core.common.queries.GetClusterFeaturesByVersionAndCategoryParameters;
import org.ovirt.engine.core.common.queries.GetConfigurationValueParameters;
import org.ovirt.engine.core.common.queries.GetConnectionsByDataCenterAndStorageTypeParameters;
import org.ovirt.engine.core.common.queries.GetDataCentersWithPermittedActionOnClustersParameters;
import org.ovirt.engine.core.common.queries.GetEntitiesWithPermittedActionParameters;
import org.ovirt.engine.core.common.queries.GetExistingStorageDomainListParameters;
import org.ovirt.engine.core.common.queries.GetHostListFromExternalProviderParameters;
import org.ovirt.engine.core.common.queries.GetHostsForStorageOperationParameters;
import org.ovirt.engine.core.common.queries.GetImagesListByStoragePoolIdParameters;
import org.ovirt.engine.core.common.queries.GetLunsByVgIdParameters;
import org.ovirt.engine.core.common.queries.GetPermittedStorageDomainsByStoragePoolIdParameters;
import org.ovirt.engine.core.common.queries.GetStorageDomainDefaultWipeAfterDeleteParameters;
import org.ovirt.engine.core.common.queries.GetStorageDomainsByConnectionParameters;
import org.ovirt.engine.core.common.queries.GetStoragePoolsByClusterServiceParameters;
import org.ovirt.engine.core.common.queries.GetSupportedCpuListParameters;
import org.ovirt.engine.core.common.queries.GetTagsByUserGroupIdParameters;
import org.ovirt.engine.core.common.queries.GetTagsByUserIdParameters;
import org.ovirt.engine.core.common.queries.GetTagsByVdsIdParameters;
import org.ovirt.engine.core.common.queries.GetTagsByVmIdParameters;
import org.ovirt.engine.core.common.queries.GetVmChangedFieldsForNextRunParameters;
import org.ovirt.engine.core.common.queries.GetVmFromOvaQueryParameters;
import org.ovirt.engine.core.common.queries.GetVmTemplateParameters;
import org.ovirt.engine.core.common.queries.GetVmTemplatesFromStorageDomainParameters;
import org.ovirt.engine.core.common.queries.GetVmsFromExternalProviderQueryParameters;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.IdsQueryParameters;
import org.ovirt.engine.core.common.queries.NameQueryParameters;
import org.ovirt.engine.core.common.queries.OsQueryParameters;
import org.ovirt.engine.core.common.queries.OsQueryParameters.OsRepositoryVerb;
import org.ovirt.engine.core.common.queries.ProviderQueryParameters;
import org.ovirt.engine.core.common.queries.QosQueryParameterBase;
import org.ovirt.engine.core.common.queries.SearchParameters;
import org.ovirt.engine.core.common.queries.ServerParameters;
import org.ovirt.engine.core.common.queries.StorageDomainsAndStoragePoolIdQueryParameters;
import org.ovirt.engine.core.common.queries.StorageServerConnectionQueryParametersBase;
import org.ovirt.engine.core.common.queries.VdcQueryParametersBase;
import org.ovirt.engine.core.common.queries.VdcQueryReturnValue;
import org.ovirt.engine.core.common.queries.VdcQueryType;
import org.ovirt.engine.core.common.queries.VmIconIdSizePair;
import org.ovirt.engine.core.common.queries.gluster.AddedGlusterServersParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterHookContentQueryParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterHookQueryParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterServersQueryParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterServiceQueryParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterVolumeAdvancedDetailsParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterVolumeGeoRepEligibilityParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterVolumeProfileParameters;
import org.ovirt.engine.core.common.queries.gluster.GlusterVolumeQueriesParameters;
import org.ovirt.engine.core.common.utils.Pair;
import org.ovirt.engine.core.common.utils.SimpleDependencyInjector;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.compat.IntegerCompat;
import org.ovirt.engine.core.compat.KeyValuePairCompat;
import org.ovirt.engine.core.compat.RefObject;
import org.ovirt.engine.core.compat.RpmVersion;
import org.ovirt.engine.core.compat.StringHelper;
import org.ovirt.engine.core.compat.Version;
import org.ovirt.engine.core.searchbackend.OsValueAutoCompleter;
import org.ovirt.engine.ui.frontend.AsyncCallback;
import org.ovirt.engine.ui.frontend.AsyncQuery;
import org.ovirt.engine.ui.frontend.Converter;
import org.ovirt.engine.ui.frontend.Frontend;
import org.ovirt.engine.ui.uicommonweb.Linq;
import org.ovirt.engine.ui.uicommonweb.comparators.QuotaComparator;
import org.ovirt.engine.ui.uicommonweb.models.ApplicationModeHelper;
import org.ovirt.engine.ui.uicommonweb.models.EntityModel;
import org.ovirt.engine.ui.uicommonweb.models.LoginModel;
import org.ovirt.engine.ui.uicommonweb.models.datacenters.NetworkModel;
import org.ovirt.engine.ui.uicommonweb.models.datacenters.NetworkQoSModel;
import org.ovirt.engine.ui.uicommonweb.models.hosts.SetupNetworksBondModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.FcpStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.GlusterStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.IStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.ImportFcpStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.ImportIscsiStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.IscsiStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.LocalStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.NfsStorageModel;
import org.ovirt.engine.ui.uicommonweb.models.storage.PosixStorageModel;
import org.ovirt.engine.ui.uicompat.ConstantsManager;
import org.ovirt.engine.ui.uicompat.EventArgs;
import org.ovirt.engine.ui.uicompat.FrontendMultipleQueryAsyncResult;
import org.ovirt.engine.ui.uicompat.IFrontendMultipleQueryAsyncCallback;

public class AsyncDataProvider {

    public static final String ISO_PREFIX = "iso://";    //$NON-NLS-1$
    private static AsyncDataProvider instance;

    public static AsyncDataProvider getInstance() {
        if (instance == null) {
            instance = new AsyncDataProvider();
        }
        return instance;
    }

    public static void setInstance(AsyncDataProvider provider) {
        instance = provider;
    }

    private static final String GENERAL = "general"; //$NON-NLS-1$

    private static int DEFAULT_OS_ID = 0;

    // dictionary to hold cache of all config values (per version) queried by client, if the request for them succeeded.
    private HashMap<KeyValuePairCompat<ConfigurationValues, String>, Object> cachedConfigValues = new HashMap<>();

    private HashMap<KeyValuePairCompat<ConfigurationValues, String>, Object> cachedConfigValuesPreConvert = new HashMap<>();

    private String _defaultConfigurationVersion = null;

    // cached OS names
    private HashMap<Integer, String> osNames;

    // OS default icons
    private Map<Integer, VmIconIdSizePair> osIdToDefaultIconIdMap;

    /**
     * large-icon-id -> small-icon-id; data comes from {@link #osIdToDefaultIconIdMap}
     */
    private Map<Guid, Guid> largeToSmallOsDefaultIconIdMap;

    // all defined migration policies
    private List<MigrationPolicy> migrationPolicies;

    // cached list of os ids
    private List<Integer> osIds;

    // cached unique OS names
    private HashMap<Integer, String> uniqueOsNames;

    // cached linux OS
    private List<Integer> linuxOsIds;

    // cached NIC hotplug support map
    private Map<Pair<Integer, Version>, Boolean> nicHotplugSupportMap;

    // cached disk hotpluggable interfaces map
    private Map<Pair<Integer, Version>, Set<String>> diskHotpluggableInterfacesMap;

    // cached os's balloon enabled by default map (given compatibility version)
    private Map<Integer, Map<Version, Boolean>> balloonSupportMap;

    // cached sound device enabled by map
    private Map<Integer, Map<Version, Boolean>> soundDeviceSupportMap;

    // cached windows OS
    private List<Integer> windowsOsIds;
    // cached OS Architecture
    private HashMap<Integer, ArchitectureType> osArchitectures;
    // default OS per architecture
    private HashMap<ArchitectureType, Integer> defaultOSes;

    // cached os's support for graphics and display types (given compatibility version)
    private Map<Integer, Map<Version, List<Pair<GraphicsType, DisplayType>>>> graphicsAndDisplays;

    // cached architecture support for live migration
    private Map<ArchitectureType, Map<Version, Boolean>> migrationSupport;

    // cached architecture support for memory snapshot
    private Map<ArchitectureType, Map<Version, Boolean>> memorySnapshotSupport;

    // cached architecture support for VM suspend
    private Map<ArchitectureType, Map<Version, Boolean>> suspendSupport;

    // cached architecture support for memory hot unplug
    private Map<ArchitectureType, Map<Version, Boolean>> memoryHotUnplugSupport;

    // cached custom properties
    private Map<Version, Map<String, String>> customPropertiesList;

    /** (CPU name, cluster compatibility version) -> {@link ServerCpu} */
    private Map<Pair<String, Version>, ServerCpu> cpuMap;

    public String getDefaultConfigurationVersion() {
        return _defaultConfigurationVersion;
    }

    private void getDefaultConfigurationVersion(final LoginModel loginModel) {
        AsyncQuery callback = new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {

            @Override
            public void onSuccess(VdcQueryReturnValue returnValue) {
                if (returnValue != null) {
                    _defaultConfigurationVersion =
                            ((VdcQueryReturnValue) returnValue).getReturnValue();
                } else {
                    _defaultConfigurationVersion = GENERAL;
                }
                loginModel.getLoggedInEvent().raise(loginModel, EventArgs.EMPTY);
            }
        });
        callback.setHandleFailure(true);
        Frontend.getInstance().runQuery(VdcQueryType.GetDefaultConfigurationVersion,
                new VdcQueryParametersBase(),
                callback);
    }

    public void initCache(final LoginModel loginModel) {
        cacheConfigValues(new AsyncQuery<>(new AsyncCallback<Map<KeyValuePairCompat<ConfigurationValues, String>, Object>>() {
            @Override
            public void onSuccess(Map<KeyValuePairCompat<ConfigurationValues, String>, Object> returnValue) {
                getDefaultConfigurationVersion(loginModel);
            }
        }));
        initOsNames();
        initOsDefaultIconIds();
        initUniqueOsNames();
        initLinuxOsTypes();
        initWindowsOsTypes();
        initDisplayTypes();
        initBalloonSupportMap();
        initNicHotplugSupportMap();
        initDiskHotpluggableInterfacesMap();
        initOsArchitecture();
        initDefaultOSes();
        initMigrationSupportMap();
        initMemorySnapshotSupportMap();
        initSuspendSupportMap();
        initMemoryHotUnplugSupportMap();
        initCustomPropertiesList();
        initSoundDeviceSupportMap();
        initMigrationPolicies();
        initCpuMap();
    }

    private void initMigrationPolicies() {
        AsyncQuery<List<MigrationPolicy>> aQuery = new AsyncQuery<>(new AsyncCallback<List<MigrationPolicy>>() {

            @Override
            public void onSuccess(List<MigrationPolicy> returnValue) {
                migrationPolicies = returnValue;
            }
        });

        aQuery.converterCallback = new Converter<List<MigrationPolicy>>() {
            @Override
            public List<MigrationPolicy> convert(Object returnValue) {
                if (returnValue == null) {
                    return new ArrayList<>();
                }

                List<MigrationPolicy> policies = (List<MigrationPolicy>) returnValue;
                Collections.sort(policies, new Comparator<MigrationPolicy>() {
                    @Override
                    public int compare(MigrationPolicy m1, MigrationPolicy m2) {
                        // the empty one is always the first
                        if (NoMigrationPolicy.ID.equals(m1.getId())) {
                            return -1;
                        }
                        return m1.getName().compareTo(m2.getName());
                    }
                });

                return policies;
            }
        };

        Frontend.getInstance().runQuery(VdcQueryType.GetAllMigrationPolicies,
                new VdcQueryParametersBase(), aQuery);
    }

    private void initCpuMap() {
        cpuMap = new HashMap<>();

        final List<VdcQueryType> queryTypes = new ArrayList<>();
        final List<VdcQueryParametersBase> queryParams = new ArrayList<>();
        for (Version version : Version.ALL) {
            queryTypes.add(VdcQueryType.GetAllServerCpuList);
            queryParams.add(new GetAllServerCpuListParameters(version));
        }

        final IFrontendMultipleQueryAsyncCallback callback = new IFrontendMultipleQueryAsyncCallback() {
            @Override
            public void executed(FrontendMultipleQueryAsyncResult result) {
                for (int i = 0; i < result.getReturnValues().size(); i++) {
                    final List<ServerCpu> cpus = result.getReturnValues().get(i).getReturnValue();
                    final Version version =
                            ((GetAllServerCpuListParameters) result.getParameters().get(i)).getVersion();
                    initCpuMapForVersion(version, cpus);
                }
            }
        };

        Frontend.getInstance().runMultipleQueries(queryTypes, queryParams, callback);
    }

    private void initCpuMapForVersion(Version version, List<ServerCpu> cpus) {
        for (ServerCpu cpu : cpus) {
            cpuMap.put(new Pair<>(cpu.getCpuName(), version), cpu);
        }
    }

    private void initCustomPropertiesList() {
        AsyncQuery<Map<Version, Map<String, String>>> callback = new AsyncQuery<>(new AsyncCallback<Map<Version, Map<String, String>>>() {
            @Override
            public void onSuccess(Map<Version, Map<String, String>> returnValue) {
                customPropertiesList = returnValue;
            }
        });

        callback.converterCallback = new MapConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmCustomProperties,
                new VdcQueryParametersBase().withoutRefresh(), callback);
    }

    public void initDefaultOSes() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetDefaultOSes), new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        defaultOSes = returnValue.getReturnValue();
                    }
                }));
    }

    public void getStorageDomainsWithAttachedStoragePoolGuid(
            AsyncQuery<List<StorageDomainStatic>> aQuery, StoragePool storagePool, List<StorageDomain> storageDomains) {
        aQuery.converterCallback = new ListConverter<>();
        StorageDomainsAndStoragePoolIdQueryParameters parameters =
                new StorageDomainsAndStoragePoolIdQueryParameters(storageDomains, storagePool.getId());
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainsWithAttachedStoragePoolGuid,
                parameters, aQuery);
    }

    public void getStorageDomainsWithAttachedStoragePoolGuid(
            AsyncQuery<List<StorageDomainStatic>> aQuery, StoragePool storagePool,
            List<StorageDomain> storageDomains, StorageServerConnections storageServerConnection, Guid vdsId) {
        aQuery.converterCallback = new ListConverter<>();

        if (storageDomains != null) {
            // Get file storage domains
            StorageDomainsAndStoragePoolIdQueryParameters parameters =
                    new StorageDomainsAndStoragePoolIdQueryParameters(storageDomains, storagePool.getId(), vdsId);
            Frontend.getInstance().runQuery(VdcQueryType.GetBlockStorageDomainsWithAttachedStoragePoolGuid,
                    parameters, aQuery);
        }
        else {
            // Get block storage domains
            StorageDomainsAndStoragePoolIdQueryParameters parameters =
                    new StorageDomainsAndStoragePoolIdQueryParameters(storageServerConnection, storagePool.getId(), vdsId);
            Frontend.getInstance().runQuery(VdcQueryType.GetFileStorageDomainsWithAttachedStoragePoolGuid,
                    parameters, aQuery);
        }
    }

    public Boolean isMigrationSupported(ArchitectureType architecture, Version version) {
        return migrationSupport.get(architecture).get(version);
    }

    public Boolean isMemorySnapshotSupportedByArchitecture(ArchitectureType architecture, Version version) {
        return memorySnapshotSupport.get(architecture).get(version);
    }

    public Boolean isSuspendSupportedByArchitecture(ArchitectureType architecture, Version version) {
        return suspendSupport.get(architecture).get(version);
    }

    public Boolean isMemoryHotUnplugSupportedByArchitecture(ArchitectureType architecture, Version version) {
        return memoryHotUnplugSupport.get(architecture).get(version);
    }

    private void initMigrationSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.GetArchitectureCapabilities,
                new ArchCapabilitiesParameters(ArchCapabilitiesVerb.GetMigrationSupport),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        migrationSupport = returnValue.getReturnValue();
                    }
                }));
    }

    private void initMemorySnapshotSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.GetArchitectureCapabilities,
                new ArchCapabilitiesParameters(ArchCapabilitiesVerb.GetMemorySnapshotSupport),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        memorySnapshotSupport = returnValue.getReturnValue();
                    }
                }));
    }

    private void initSuspendSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.GetArchitectureCapabilities,
                new ArchCapabilitiesParameters(ArchCapabilitiesVerb.GetSuspendSupport),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        suspendSupport = returnValue.getReturnValue();
                    }
                }));
    }

    private void initMemoryHotUnplugSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.GetArchitectureCapabilities,
                new ArchCapabilitiesParameters(ArchCapabilitiesVerb.GetMemoryHotUnplugSupport),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        memoryHotUnplugSupport = returnValue.getReturnValue();
                    }
                }));
    }

    /**
     * Check if memory snapshot is supported
     */
    public boolean isMemorySnapshotSupported(VM vm) {
        if (vm == null) {
            return false;
        }

        return isMemorySnapshotSupportedByArchitecture(
                vm.getClusterArch(),
                vm.getCompatibilityVersion());
    }

    public boolean isMemoryHotUnplugSupported(VM vm) {
        if (vm == null) {
            return false;
        }

        return isMemoryHotUnplugSupportedByArchitecture(
                vm.getClusterArch(),
                vm.getCompatibilityVersion());
    }

    public boolean canVmsBePaused(List<VM> items) {
        for (VM vm : items) {
            if (!isSuspendSupportedByArchitecture(vm.getClusterArch(),
                    vm.getCompatibilityVersion())) {
                return false;
            }
        }

        return true;
    }

    public void initNicHotplugSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetNicHotplugSupportMap), new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        nicHotplugSupportMap = returnValue.getReturnValue();
                    }
                }));
    }

    public Map<Pair<Integer, Version>, Boolean> getNicHotplugSupportMap() {
        return nicHotplugSupportMap;
    }

    public Boolean getNicHotplugSupport(Integer osId, Version version) {
        Pair<Integer, Version> pair = new Pair<>(osId, version);

        if (getNicHotplugSupportMap().containsKey(pair)) {
            return getNicHotplugSupportMap().get(pair);
        }

        return false;
    }

    public Boolean isBalloonEnabled(int osId, Version version) {
        return balloonSupportMap.get(osId).get(version);
    }

    public void initBalloonSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetBalloonSupportMap), new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        balloonSupportMap = returnValue.getReturnValue();
                    }
                }));
    }

    public void initDiskHotpluggableInterfacesMap() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetDiskHotpluggableInterfacesMap), new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        diskHotpluggableInterfacesMap = returnValue.getReturnValue();
                    }
                }));
    }

    public Boolean isSoundDeviceEnabled(int osId, Version version) {
        return soundDeviceSupportMap.get(osId).get(version);
    }

    public void initSoundDeviceSupportMap() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetSoundDeviceSupportMap), new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue result) {
                        soundDeviceSupportMap = result.getReturnValue();
                    }
                }));
    }

    public Map<Pair<Integer, Version>, Set<String>> getDiskHotpluggableInterfacesMap() {
        return diskHotpluggableInterfacesMap;
    }

    public Collection<DiskInterface> getDiskHotpluggableInterfaces(Integer osId, Version version) {

        Set<String> diskHotpluggableInterfaces = getDiskHotpluggableInterfacesMap().get(new Pair<>(osId, version));
        if (diskHotpluggableInterfaces == null) {
            return Collections.emptySet();
        }

        Collection<DiskInterface> diskInterfaces = new HashSet<>();
        for (String diskHotpluggableInterface : diskHotpluggableInterfaces) {
            diskInterfaces.add(DiskInterface.valueOf(diskHotpluggableInterface));
        }

        return diskInterfaces;
    }

    public void getUserProfile(AsyncQuery<VdcQueryReturnValue> aQuery) {
        Frontend.getInstance().runQuery(VdcQueryType.GetUserProfile, new VdcQueryParametersBase().withoutRefresh(), aQuery);
    }

    public void getAAAProfilesListViaPublic(AsyncQuery<List<String>> aQuery, boolean passwordBasedOnly) {
        convertAAAProfilesResult(aQuery, passwordBasedOnly);
        Frontend.getInstance().runPublicQuery(VdcQueryType.GetAAAProfileList, new VdcQueryParametersBase(), aQuery);
    }

    public static void isFloppySupported(AsyncQuery<Boolean> aQuery, Integer osId, Version version) {
        aQuery.converterCallback = new DefaultValueConverter<>(Boolean.FALSE);
        OsQueryParameters params = new OsQueryParameters(OsRepositoryVerb.GetFloppySupport, osId, version);

        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, params, aQuery);
    }

    public void getIsoDomainByDataCenterId(AsyncQuery<StorageDomain> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new Converter<StorageDomain>() {
            @Override
            public StorageDomain convert(Object source) {
                if (source != null) {
                    ArrayList<StorageDomain> storageDomains = (ArrayList<StorageDomain>) source;
                    for (StorageDomain domain : storageDomains) {
                        if (domain.getStorageDomainType() == StorageDomainType.ISO) {
                            return domain;
                        }
                    }
                }

                return null;
            }
        };

        IdQueryParameters getIsoParams = new IdQueryParameters(dataCenterId);
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainsByStoragePoolId, getIsoParams, aQuery);
    }

    public void getExportDomainByDataCenterId(AsyncQuery<StorageDomain> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new Converter<StorageDomain>() {
            @Override
            public StorageDomain convert(Object source) {
                ArrayList<StorageDomain> storageDomains = (ArrayList<StorageDomain>) source;
                for (StorageDomain domain : storageDomains) {
                    if (domain.getStorageDomainType() == StorageDomainType.ImportExport) {
                        return domain;
                    }
                }

                return null;
            }
        };

        IdQueryParameters getExportParams = new IdQueryParameters(dataCenterId);
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainsByStoragePoolId, getExportParams, aQuery);
    }

    public void getIrsImageList(AsyncQuery<List<String>> aQuery, Guid storagePoolId) {
        getIrsImageList(aQuery, storagePoolId, false);
    }

    public void getIrsImageList(AsyncQuery<List<String>> aQuery, Guid storagePoolId, boolean forceRefresh) {
        ImageFileType imageFileType = ImageFileType.ISO;
        getIrsImageList(aQuery, storagePoolId, forceRefresh, imageFileType);
    }

    public void getFloppyImageList(AsyncQuery<List<String>> aQuery, Guid storagePoolId) {
        getIrsImageList(aQuery, storagePoolId, false, ImageFileType.Floppy);
    }

    public void getUnknownImageList(AsyncQuery<List<String>> aQuery, Guid storagePoolId, boolean forceRefresh) {
        getIrsImageList(aQuery,
                storagePoolId,
                forceRefresh,
                ImageFileType.All,
                new RepoImageToImageFileNameAsyncConverter() {


                    @Override
                    protected String transform(ArrayList<String> fileNameList, RepoImage repoImage) {
                        return ISO_PREFIX + super.transform(fileNameList, repoImage);
                    }

                    @Override
                    protected boolean desiredImage(RepoImage repoImage) {
                        return ImageFileType.Unknown == repoImage.getFileType();
                    }
                });
    }

    public void getIrsImageList(AsyncQuery<List<String>> aQuery,
            Guid storagePoolId,
            boolean forceRefresh,
            ImageFileType imageFileType) {


        getIrsImageList(aQuery, storagePoolId, forceRefresh, imageFileType,
                new RepoImageToImageFileNameAsyncConverter());
    }

    private void getIrsImageList(AsyncQuery<List<String>> aQuery,
            Guid storagePoolId,
            boolean forceRefresh,
            ImageFileType imageFileType,
            Converter<List<String>> converterCallBack) {

        aQuery.converterCallback = converterCallBack;

        GetImagesListByStoragePoolIdParameters parameters =
                new GetImagesListByStoragePoolIdParameters(storagePoolId, imageFileType);
        parameters.setForceRefresh(forceRefresh);
        Frontend.getInstance().runQuery(VdcQueryType.GetImagesListByStoragePoolId, parameters, aQuery);
    }

    public void getDefaultManagementNetwork(AsyncQuery<Network> aQuery, Guid dataCenterId) {
        runQueryByIdParameter(VdcQueryType.GetDefaultManagementNetwork, aQuery, dataCenterId);
    }

    public void getManagementNetwork(AsyncQuery<Network> aQuery, Guid clusterId) {
        runQueryByIdParameter(VdcQueryType.GetManagementNetwork, aQuery, clusterId);
    }

    public void isManagementNetwork(AsyncQuery<Boolean> aQuery, Guid networkId) {
        runQueryByIdParameter(VdcQueryType.IsManagementNetwork, aQuery, networkId);
    }

    public void isClusterEmpty(AsyncQuery<Boolean> aQuery, Guid clusterId) {
        runQueryByIdParameter(VdcQueryType.IsClusterEmpty, aQuery, clusterId);
    }

    private void runQueryByIdParameter(VdcQueryType queryType, AsyncQuery aQuery, Guid id) {
        aQuery.converterCallback = new CastingConverter();
        Frontend.getInstance().runQuery(queryType, new IdQueryParameters(id), aQuery);
    }

    public void getHostArchitecture(AsyncQuery<ArchitectureType> aQuery, Guid id) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetHostArchitecture, new IdQueryParameters(id), aQuery);
    }

    public void getClusterById(AsyncQuery<Cluster> aQuery, Guid id) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetClusterById, new IdQueryParameters(id), aQuery);
    }

    public void getClusterListByName(AsyncQuery<List<Cluster>> aQuery, String name) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.Search,
                new SearchParameters("Cluster: name=" + name + " sortby name", SearchType.Cluster), //$NON-NLS-1$ //$NON-NLS-2$
                aQuery);
    }

    public void getAuthzGroupsByUserId(AsyncQuery<List<AuthzGroup>> aQuery, Guid userId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAuthzGroupsByUserId, new IdQueryParameters(userId), aQuery);
    }

    public void getPoolById(AsyncQuery<VmPool> aQuery, Guid poolId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmPoolById, new IdQueryParameters(poolId), aQuery);
    }

    public void getVmById(AsyncQuery<VM> aQuery, Guid vmId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmByVmId, new IdQueryParameters(vmId), aQuery);
    }

    public void getVmNextRunConfiguration(AsyncQuery<VM> aQuery, Guid vmId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmNextRunConfiguration, new IdQueryParameters(vmId), aQuery);
    }

    public void getVmChangedFieldsForNextRun(VM original, VM updated, VmManagementParametersBase updateVmParameters, AsyncQuery<VdcQueryReturnValue> aQuery) {
        Frontend.getInstance().runQuery(VdcQueryType.GetVmChangedFieldsForNextRun,
                new GetVmChangedFieldsForNextRunParameters(original, updated, updateVmParameters), aQuery);
    }

    public void getDataCenterList(AsyncQuery<List<StoragePool>> aQuery) {
        getDataCenterList(aQuery, true);
    }

    public List<MigrationPolicy> getMigrationPolicies() {
        return migrationPolicies;
    }

    public void getDataCenterList(AsyncQuery<List<StoragePool>> aQuery, boolean doRefresh) {
        aQuery.converterCallback = new ListConverter<>();
        SearchParameters params = new SearchParameters("DataCenter: sortby name", SearchType.StoragePool); //$NON-NLS-1$
        Frontend.getInstance().runQuery(VdcQueryType.Search, doRefresh ? params : params.withoutRefresh(), aQuery);
    }

    public void getDataCenterByClusterServiceList(AsyncQuery<List<StoragePool>> aQuery,
            boolean supportsVirtService,
            boolean supportsGlusterService) {
        aQuery.converterCallback = new SortListByNameConverter<>();

        final GetStoragePoolsByClusterServiceParameters parameters = new GetStoragePoolsByClusterServiceParameters();
        parameters.setSupportsVirtService(supportsVirtService);
        parameters.setSupportsGlusterService(supportsGlusterService);

        Frontend.getInstance().runQuery(VdcQueryType.GetStoragePoolsByClusterService, parameters, aQuery);
    }

    public void getDataCenterListByName(AsyncQuery<List<StoragePool>> aQuery, String name) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.Search,
                new SearchParameters("DataCenter: name=" + name + " sortby name", SearchType.StoragePool), //$NON-NLS-1$ //$NON-NLS-2$
                aQuery);
    }

    public void getSpiceUsbAutoShare(AsyncQuery<Boolean> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(Boolean.TRUE);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.SpiceUsbAutoShare,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getConfigurationValueBoolean(AsyncQuery<Boolean> aQuery, ConfigurationValues configVal) {
        aQuery.converterCallback = new DefaultValueConverter<>(Boolean.TRUE);
        getConfigFromCache(
                new GetConfigurationValueParameters(configVal, getDefaultConfigurationVersion()), aQuery);
    }

    public void getWANColorDepth(AsyncQuery<WanColorDepth> aQuery) {
        aQuery.converterCallback = new Converter<WanColorDepth>() {
            @Override
            public WanColorDepth convert(Object source) {
                return source != null ? WanColorDepth.fromInt((Integer) source) : WanColorDepth.depth16;
            }
        };
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.WANColorDepth, getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getWANDisableEffects(AsyncQuery<List<WanDisableEffects>> aQuery) {
        aQuery.converterCallback = new Converter<List<WanDisableEffects>>() {
            @Override
            public List<WanDisableEffects> convert(Object source) {
                if (source == null) {
                    return new ArrayList<>();
                }

                List<WanDisableEffects> res = new ArrayList<>();
                String fromDb = (String) source;
                for (String value : fromDb.split(",")) {//$NON-NLS-1$
                    if (value == null) {
                        continue;
                    }

                    String trimmedValue = value.trim();
                    if ("".equals(trimmedValue)) {
                        continue;
                    }

                    res.add(WanDisableEffects.fromString(trimmedValue));
                }

                return res;

            }
        };
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.WANDisableEffects,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public ServerCpu getCpuByName(String cpuName, Version clusterVersion) {
        return cpuMap.get(new Pair<>(cpuName, clusterVersion));
    }

    public void getMaxVmsInPool(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(1000);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.MaxVmsInPool, getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getMaxNumOfVmSockets(AsyncQuery<Integer> aQuery, String version) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        GetConfigurationValueParameters tempVar =
                new GetConfigurationValueParameters(ConfigurationValues.MaxNumOfVmSockets);
        tempVar.setVersion(version);
        getConfigFromCache(tempVar, aQuery);
    }

    public void getMaxNumOfVmCpus(AsyncQuery<Integer> aQuery, String version) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        GetConfigurationValueParameters tempVar =
                new GetConfigurationValueParameters(ConfigurationValues.MaxNumOfVmCpus);
        tempVar.setVersion(version);
        getConfigFromCache(tempVar, aQuery);
    }

    public void getMaxNumOfCPUsPerSocket(AsyncQuery<Integer> aQuery, String version) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        GetConfigurationValueParameters tempVar =
                new GetConfigurationValueParameters(ConfigurationValues.MaxNumOfCpuPerSocket);
        tempVar.setVersion(version);
        getConfigFromCache(tempVar, aQuery);
    }

    public void getMaxNumOfThreadsPerCpu(AsyncQuery<Integer> aQuery, String version) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        GetConfigurationValueParameters tempVar =
                new GetConfigurationValueParameters(ConfigurationValues.MaxNumOfThreadsPerCpu);
        tempVar.setVersion(version);
        getConfigFromCache(tempVar, aQuery);
    }

    public void getClusterList(AsyncQuery<List<Cluster>> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetClustersByStoragePoolId,
                new IdQueryParameters(dataCenterId),
                aQuery);
    }

    public List<Cluster> filterByArchitecture(List<Cluster> clusters, ArchitectureType targetArchitecture) {
        List<Cluster> filteredClusters = new ArrayList<>();

        for (Cluster cluster : clusters) {
            if (cluster.getArchitecture().equals(targetArchitecture)) {
                filteredClusters.add(cluster);
            }
        }
        return filteredClusters;
    }

    public List<Cluster> filterClustersWithoutArchitecture(List<Cluster> clusters) {
        List<Cluster> filteredClusters = new ArrayList<>();

        for (Cluster cluster : clusters) {
            if (cluster.getArchitecture() != ArchitectureType.undefined) {
                filteredClusters.add(cluster);
            }
        }
        return filteredClusters;
    }

    public void getClusterByServiceList(AsyncQuery<List<Cluster>> aQuery, Guid dataCenterId,
            final boolean supportsVirtService, final boolean supportsGlusterService) {
        aQuery.converterCallback = new Converter<List<Cluster>>() {
            @Override
            public List<Cluster> convert(Object source) {
                if (source == null) {
                    return new ArrayList<>();
                }
                final ArrayList<Cluster> list = (ArrayList<Cluster>) source;
                return getClusterByServiceList(list, supportsVirtService, supportsGlusterService);
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetClustersByStoragePoolId,
                new IdQueryParameters(dataCenterId),
                aQuery);
    }

    public void isSoundcardEnabled(AsyncQuery<Boolean> aQuery, Guid vmId) {
        aQuery.converterCallback = new IsNonEmptyCollectionConverter();
        Frontend.getInstance().runQuery(VdcQueryType.GetSoundDevices, new IdQueryParameters(vmId), aQuery);
    }

    public void isVirtioScsiEnabledForVm(AsyncQuery<Boolean> aQuery, Guid vmId) {
        aQuery.converterCallback = new IsNonEmptyCollectionConverter();
        Frontend.getInstance().runQuery(VdcQueryType.GetVirtioScsiControllers, new IdQueryParameters(vmId), aQuery);
    }

    public void getClusterListByService(AsyncQuery<List<Cluster>> aQuery, final boolean supportsVirtService,
            final boolean supportsGlusterService) {

        aQuery.converterCallback = new Converter<List<Cluster>>() {
            @Override
            public List<Cluster> convert(Object source) {
                if (source != null) {
                    ArrayList<Cluster> list =
                            getClusterByServiceList((ArrayList<Cluster>) source,
                                    supportsVirtService,
                                    supportsGlusterService);
                    Collections.sort(list, new NameableComparator());
                    return list;
                }
                return new ArrayList<>();
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetAllClusters, new VdcQueryParametersBase(), aQuery);
    }

    public void getClusterList(AsyncQuery<List<Cluster>> aQuery) {
        getClusterList(aQuery, true);
    }

    public void getClusterList(AsyncQuery aQuery, boolean doRefresh) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllClusters, doRefresh ? new VdcQueryParametersBase() :
                new VdcQueryParametersBase().withoutRefresh(), aQuery);
    }

    public void getLabelList(AsyncQuery<List<Label>> aQuery) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllLabels, new VdcQueryParametersBase(), aQuery);
    }

    public void getLabelListByEntityId(AsyncQuery<List<Label>> aQuery, Guid entityId) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetLabelByEntityId, new IdQueryParameters(entityId), aQuery);
    }

    public void getTemplateDiskList(AsyncQuery<List<DiskImage>> aQuery, Guid templateId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplatesDisks, new IdQueryParameters(templateId), aQuery);
    }

    /**
     * Round the priority to the closest value from n (3 for now) values
     *
     * i.e.: if priority entered is 30 and the predefined values are 1,50,100
     *
     * then the return value will be 50 (closest to 50).
     *
     * @param priority
     *            - the current priority of the vm
     * @param maxPriority
     *            - the max priority
     * @return the rounded priority
     */
    public int getRoundedPriority(int priority, int maxPriority) {

        int medium = maxPriority / 2;

        int[] levels = new int[] { 1, medium, maxPriority };

        for (int i = 0; i < levels.length; i++) {
            int lengthToLess = levels[i] - priority;
            int lengthToMore = levels[i + 1] - priority;

            if (lengthToMore < 0) {
                continue;
            }

            return Math.abs(lengthToLess) < lengthToMore ? levels[i] : levels[i + 1];
        }

        return 0;
    }

    public void getTemplateListByDataCenter(AsyncQuery<List<VmTemplate>> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new TemplateConverter();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplatesByStoragePoolId,
                new IdQueryParameters(dataCenterId),
                aQuery);
    }

    public void getTemplateListByStorage(AsyncQuery<List<VmTemplate>> aQuery, Guid storageId) {
        aQuery.converterCallback = new Converter<List<VmTemplate>>() {
            @Override
            public List<VmTemplate> convert(Object source) {
                ArrayList<VmTemplate> list = new ArrayList<>();
                if (source != null) {
                    for (VmTemplate template : (ArrayList<VmTemplate>) source) {
                        if (template.getStatus() == VmTemplateStatus.OK) {
                            list.add(template);
                        }
                    }

                    Collections.sort(list, new NameableComparator());
                }

                return list;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplatesFromStorageDomain,
                new GetVmTemplatesFromStorageDomainParameters(storageId, false),
                aQuery);
    }

    public ArrayList<VmTemplate> filterTemplatesByArchitecture(List<VmTemplate> list,
            ArchitectureType architecture) {
        ArrayList<VmTemplate> filteredList = new ArrayList<>();

        for (VmTemplate template : list) {
            if (template.getId().equals(Guid.Empty) ||
                    template.getClusterArch().equals(architecture)) {
                filteredList.add(template);
            }
        }

        return filteredList;
    }

    public void getNumOfMonitorList(AsyncQuery<List<Integer>> aQuery) {
        aQuery.converterCallback = new Converter<List<Integer>>() {
            @Override
            public List<Integer> convert(Object source) {
                ArrayList<Integer> nums = new ArrayList<>();
                if (source != null) {
                    Iterable numEnumerable = (Iterable) source;
                    Iterator numIterator = numEnumerable.iterator();
                    while (numIterator.hasNext()) {
                        nums.add(Integer.parseInt(numIterator.next().toString()));
                    }
                }
                return nums;
            }
        };
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.ValidNumOfMonitors,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getStorageDomainList(AsyncQuery<List<StorageDomain>> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainsByStoragePoolId,
                new IdQueryParameters(dataCenterId),
                aQuery);
    }

    public void getMaxVmPriority(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(100);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.VmPriorityMaxValue,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getHostById(AsyncQuery<VDS> aQuery, Guid id) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVdsByVdsId, new IdQueryParameters(id).withoutRefresh(), aQuery);
    }

    public void getHostListByCluster(AsyncQuery<List<VDS>> aQuery, String clusterName) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.Search, new SearchParameters("Host: cluster = " + clusterName + " sortby name", //$NON-NLS-1$ //$NON-NLS-2$
                SearchType.VDS), aQuery);
    }

    public void getHostListByClusterId(AsyncQuery<List<VDS>> aQuery, Guid clusterId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetHostsByClusterId, new IdQueryParameters(clusterId), aQuery);
    }

    public void getHostListByDataCenter(AsyncQuery<List<VDS>> aQuery, Guid spId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllVdsByStoragePool, new IdQueryParameters(spId), aQuery);
    }

    public void getHostDevicesByHostId(AsyncQuery<List<HostDeviceView>> aQuery, Guid hostId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetExtendedHostDevicesByHostId, new IdQueryParameters(hostId), aQuery);
    }

    public void getConfiguredVmHostDevices(AsyncQuery<List<VmHostDevice>> aQuery, Guid vmId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmHostDevices, new IdQueryParameters(vmId), aQuery);
    }

    public void getVmDiskList(AsyncQuery<List<Disk>> aQuery, Guid vmId, boolean isRefresh) {
        aQuery.converterCallback = new ListConverter<>();
        IdQueryParameters params = new IdQueryParameters(vmId);
        params.setRefresh(isRefresh);
        Frontend.getInstance().runQuery(VdcQueryType.GetAllDisksByVmId, params, aQuery);
    }

    public HashMap<Integer, String> getOsUniqueOsNames() {
        return uniqueOsNames;
    }

    public void getAAAProfilesList(AsyncQuery<List<String>> aQuery) {
        convertAAAProfilesResult(aQuery, false);
        Frontend.getInstance().runQuery(VdcQueryType.GetAAAProfileList, new VdcQueryParametersBase(), aQuery);
    }

    public void getAAANamespaces(AsyncQuery<Map<String, List<String>>> aQuery) {
        aQuery.converterCallback = new MapConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAvailableNamespaces, new VdcQueryParametersBase(), aQuery);
    }


    public void getAAAProfilesEntriesList(AsyncQuery<List<ProfileEntry>> aQuery) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAAAProfileList, new VdcQueryParametersBase(), aQuery);
    }

    public void getRoleList(AsyncQuery<List<Role>> aQuery) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllRoles, new VdcQueryParametersBase(), aQuery);
    }

    public void getStorageDomainById(AsyncQuery<StorageDomain> aQuery, Guid storageDomainId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainById,
                new IdQueryParameters(storageDomainId),
                aQuery);
    }

    public void getStorageDomainByName(AsyncQuery<StorageDomainStatic> aQuery, String storageDomainName) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainByName,
                new NameQueryParameters(storageDomainName),
                aQuery);
    }

    public VolumeFormat getDiskVolumeFormat(VolumeType volumeType, StorageType storageType) {
        if (storageType.isFileDomain()) {
            return VolumeFormat.RAW;
        } else if (storageType.isBlockDomain()) {
            switch (volumeType) {
            case Sparse:
                return VolumeFormat.COW;

            case Preallocated:
                return VolumeFormat.RAW;

            default:
                return VolumeFormat.Unassigned;
            }
        } else {
            return VolumeFormat.Unassigned;
        }
    }

    public VolumeType getVolumeType(VolumeFormat volumeFormat, StorageType storageType) {
        switch (volumeFormat) {
        case COW:
            return VolumeType.Sparse;
        case RAW:
        default:
            return storageType.isFileDomain() ? VolumeType.Sparse : VolumeType.Preallocated;
        }
    }

    public void getClusterNetworkList(AsyncQuery<List<Network>> aQuery, Guid clusterId) {
        // do not replace a converter = just add if none provided
        if (aQuery.converterCallback == null) {
            aQuery.converterCallback = new ListConverter<>();
        }

        Frontend.getInstance().runQuery(VdcQueryType.GetAllNetworksByClusterId, new IdQueryParameters(clusterId), aQuery);
    }

    public void getAllNetworkQos(Guid dcId, AsyncQuery<List<NetworkQoS>> query) {
        query.converterCallback = new ListConverter<NetworkQoS>() {

            @Override
            public List<NetworkQoS> convert(Object returnValue) {
                List<NetworkQoS> qosList = super.convert(returnValue);
                qosList.add(0, NetworkQoSModel.EMPTY_QOS);
                return qosList;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetAllNetworkQosByStoragePoolId, new IdQueryParameters(dcId), query);
    }

    public void getAllHostNetworkQos(Guid dcId, AsyncQuery<List<HostNetworkQos>> query) {
        query.converterCallback = new ListConverter<HostNetworkQos>() {

            @Override
            public List<HostNetworkQos> convert(Object returnValue) {
                List<HostNetworkQos> qosList = super.convert(returnValue);
                qosList.add(0, NetworkModel.EMPTY_HOST_NETWORK_QOS);
                return qosList;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetAllQosByStoragePoolIdAndType,
                new QosQueryParameterBase(dcId, QosType.HOSTNETWORK),
                query);
    }

    public void getDataCenterById(AsyncQuery<StoragePool> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetStoragePoolById,
                new IdQueryParameters(dataCenterId).withoutRefresh(), aQuery);
    }

    public void getNetworkLabelsByDataCenterId(Guid dataCenterId, AsyncQuery<SortedSet<String>> query) {
        query.converterCallback = new Converter<SortedSet<String>>() {
            @Override
            public SortedSet<String> convert(Object returnValue) {
                SortedSet<String> sortedSet = new TreeSet<>(new LexoNumericComparator());
                sortedSet.addAll((Collection<String>) returnValue);
                return sortedSet;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetNetworkLabelsByDataCenterId,
                new IdQueryParameters(dataCenterId),
                query);
    }

    public void getWatchdogByVmId(AsyncQuery aQuery, Guid vmId) {
        Frontend.getInstance().runQuery(VdcQueryType.GetWatchdog, new IdQueryParameters(vmId), aQuery);
    }

    public void getTemplateById(AsyncQuery<VmTemplate> aQuery, Guid templateId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplate, new GetVmTemplateParameters(templateId), aQuery);
    }

    public void countAllTemplates(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplateCount, new VdcQueryParametersBase(), aQuery);
    }

    public void getHostList(AsyncQuery<List<VDS>> aQuery) {
        getHostListByStatus(aQuery, null);
    }

    public void getHostList(AsyncQuery<List<VDS>> aQuery, boolean doRefresh) {
        getHostListByStatus(aQuery, null, doRefresh);
    }

    public void getHostListByStatus(AsyncQuery<List<VDS>> aQuery, VDSStatus status) {
        getHostListByStatus(aQuery, status, true);
    }

    public void getHostListByStatus(AsyncQuery<List<VDS>> aQuery, VDSStatus status, boolean doRefresh) {
        aQuery.converterCallback = new ListConverter<>();
        SearchParameters searchParameters =
                new SearchParameters("Host: " + (status == null ? "" : ("status=" + status.name())), SearchType.VDS); //$NON-NLS-1$ //$NON-NLS-2$
        searchParameters.setMaxCount(9999);
        Frontend.getInstance().runQuery(VdcQueryType.Search,
                doRefresh ? searchParameters : searchParameters.withoutRefresh(), aQuery);
    }

    public void getHostsForStorageOperation(AsyncQuery<List<VDS>> aQuery, Guid storagePoolId, boolean localFsOnly) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetHostsForStorageOperation,
                new GetHostsForStorageOperationParameters(storagePoolId, localFsOnly),
                aQuery);
    }

    public void getVolumeList(AsyncQuery<List<GlusterVolumeEntity>> aQuery, String clusterName) {
        getVolumeList(aQuery, clusterName, true);
    }

    public void getVolumeList(AsyncQuery<List<GlusterVolumeEntity>> aQuery, String clusterName, boolean doRefresh) {

        if ((ApplicationModeHelper.getUiMode().getValue() & ApplicationMode.GlusterOnly.getValue()) == 0) {
            aQuery.getAsyncCallback().onSuccess(new ArrayList<GlusterVolumeEntity>());
            return;
        }
        aQuery.converterCallback = new ListConverter<>();
        SearchParameters searchParameters;
        searchParameters =
                clusterName == null ? new SearchParameters("Volumes:", SearchType.GlusterVolume) //$NON-NLS-1$
                        : new SearchParameters("Volumes: cluster.name=" + clusterName, SearchType.GlusterVolume); //$NON-NLS-1$
        searchParameters.setMaxCount(9999);
        if (!doRefresh) {
            searchParameters.withoutRefresh();
        }
        Frontend.getInstance().runQuery(VdcQueryType.Search, searchParameters, aQuery);
    }

    public void getGlusterVolumeOptionInfoList(AsyncQuery aQuery, Guid clusterId) {
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeOptionsInfo, new GlusterParameters(clusterId), aQuery);
    }

    public void getHostFingerprint(AsyncQuery<String> aQuery, String hostAddress) {
        aQuery.converterCallback = new StringConverter();
        Frontend.getInstance().runQuery(VdcQueryType.GetServerSSHKeyFingerprint, new ServerParameters(hostAddress), aQuery);
    }

    public void getEngineSshPublicKey(AsyncQuery<String> aQuery) {
        aQuery.converterCallback = new StringConverter();
        Frontend.getInstance().runQuery(VdcQueryType.GetEngineSSHPublicKey, new VdcQueryParametersBase(), aQuery);
    }

    public void getGlusterHosts(AsyncQuery aQuery, String hostAddress, String rootPassword, String fingerprint) {
        GlusterServersQueryParameters parameters = new GlusterServersQueryParameters(hostAddress, rootPassword);
        parameters.setFingerprint(fingerprint);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterServersForImport,
                parameters,
                aQuery);
    }

    public void getClusterGlusterServices(AsyncQuery<GlusterVolumeAdvancedDetails> aQuery, Guid clusterId) {
        aQuery.converterCallback = new CastingConverter<>();
        // Passing empty values for Volume and Brick to get the services of all the volumes/hosts in the cluster
        GlusterVolumeAdvancedDetailsParameters parameters =
                new GlusterVolumeAdvancedDetailsParameters(clusterId, null, null, false); //$NON-NLS-1$ //$NON-NLS-2$
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeAdvancedDetails,
                parameters,
                aQuery);
    }

    public void getGlusterVolumeBrickDetails(AsyncQuery aQuery, Guid clusterId, Guid volumeId, Guid brickId) {
        GlusterVolumeAdvancedDetailsParameters parameters =
                new GlusterVolumeAdvancedDetailsParameters(clusterId, volumeId, brickId, true);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeAdvancedDetails,
                parameters,
                aQuery);
    }

    public void getGlusterHostsNewlyAdded(AsyncQuery<Map<String, String>> aQuery, Guid clusterId, boolean isFingerprintRequired) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAddedGlusterServers,
                new AddedGlusterServersParameters(clusterId, isFingerprintRequired),
                aQuery);
    }

    public void isAnyHostUpInCluster(AsyncQuery<Boolean> aQuery, String clusterName) {
        aQuery.converterCallback = new IsNonEmptyCollectionConverter();
        getUpHostListByCluster(aQuery, clusterName, 1);
    }

    public void getGlusterHooks(AsyncQuery<List<GlusterHookEntity>> aQuery, Guid clusterId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterHooks, new GlusterParameters(clusterId), aQuery);
    }

    public void getGlusterBricksForServer(AsyncQuery<List<GlusterBrickEntity>> aQuery, Guid serverId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeBricksByServerId, new IdQueryParameters(serverId), aQuery);
    }

    public void getGlusterVolumeGeoRepStatusForMasterVolume(AsyncQuery<List<GlusterGeoRepSession>> aQuery, Guid masterVolumeId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeGeoRepSessions, new IdQueryParameters(masterVolumeId), aQuery);
    }

    public void getGlusterVolumeGeoRepRecommendationViolations(AsyncQuery<List<GlusterGeoRepNonEligibilityReason>> aQuery,
            Guid masterVolumeId,
            Guid slaveVolumeId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetNonEligibilityReasonsOfVolumeForGeoRepSession,
                new GlusterVolumeGeoRepEligibilityParameters(masterVolumeId, slaveVolumeId),
                aQuery);
    }

    public void getGlusterVolumeSnapshotsForVolume(AsyncQuery<List<GlusterVolumeSnapshotEntity>> aQuery, Guid volumeId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeSnapshotsByVolumeId, new IdQueryParameters(volumeId), aQuery);
    }

    public void getVolumeSnapshotSchedule(AsyncQuery<GlusterVolumeSnapshotSchedule> aQuery, Guid volumeId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeSnapshotScheduleByVolumeId,
                new IdQueryParameters(volumeId),
                aQuery);
    }

    public void getIsGlusterVolumeSnapshotCliScheduleEnabled(AsyncQuery<Boolean> aQuery, Guid clusterId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeSnapshotCliScheduleFlag,
                new IdQueryParameters(clusterId),
                aQuery);
    }

    public void getGlusterHook(AsyncQuery<GlusterHookEntity> aQuery, Guid hookId, boolean includeServerHooks) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterHookById,
                new GlusterHookQueryParameters(hookId, includeServerHooks),
                aQuery);
    }

    public void getGlusterHookContent(AsyncQuery<String> aQuery, Guid hookId, Guid serverId) {
        aQuery.converterCallback = new StringConverter();
        GlusterHookContentQueryParameters parameters = new GlusterHookContentQueryParameters(hookId);
        parameters.setGlusterServerId(serverId);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterHookContent, parameters, aQuery);
    }

    public void getGlusterSwiftServices(AsyncQuery<List<GlusterServerService>> aQuery, Guid serverId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterServerServicesByServerId, new GlusterServiceQueryParameters(serverId,
                ServiceType.GLUSTER_SWIFT), aQuery);
    }

    public void getClusterGlusterSwiftService(AsyncQuery<GlusterClusterService> aQuery, Guid clusterId) {

        aQuery.converterCallback = new GetFirstConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterClusterServiceByClusterId,
                new GlusterServiceQueryParameters(clusterId,
                        ServiceType.GLUSTER_SWIFT), aQuery);
    }

    public void getGlusterSwiftServerServices(AsyncQuery<List<GlusterServerService>> aQuery, Guid clusterId) {

        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterServerServicesByClusterId,
                new GlusterServiceQueryParameters(clusterId,
                        ServiceType.GLUSTER_SWIFT), aQuery);
    }

    public void getGlusterRebalanceStatus(AsyncQuery<VdcQueryReturnValue> aQuery, Guid clusterId, Guid volumeId) {
        aQuery.setHandleFailure(true);
        GlusterVolumeQueriesParameters parameters = new GlusterVolumeQueriesParameters(clusterId, volumeId);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeRebalanceStatus, parameters, aQuery);
    }

    public void getGlusterSnapshotConfig(AsyncQuery<VdcQueryReturnValue> aQuery, Guid clusterId, Guid volumeId) {
        aQuery.setHandleFailure(true);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeSnapshotConfig, new GlusterVolumeQueriesParameters(clusterId, volumeId), aQuery);
    }

    public void getGlusterVolumeProfilingStatistics(AsyncQuery<VdcQueryReturnValue> aQuery, Guid clusterId, Guid volumeId, boolean nfs) {
        aQuery.setHandleFailure(true);
        GlusterVolumeProfileParameters parameters = new GlusterVolumeProfileParameters(clusterId, volumeId, nfs);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeProfileInfo, parameters, aQuery);
    }

    public void getGlusterRemoveBricksStatus(AsyncQuery<VdcQueryReturnValue> aQuery,
            Guid clusterId,
            Guid volumeId,
            List<GlusterBrickEntity> bricks) {
        aQuery.setHandleFailure(true);
        GlusterVolumeRemoveBricksQueriesParameters parameters =
                new GlusterVolumeRemoveBricksQueriesParameters(clusterId, volumeId, bricks);
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterVolumeRemoveBricksStatus, parameters, aQuery);
    }

    public void getSearchResultsLimit(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(100);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.SearchResultsLimit,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public Map<Version, Map<String, String>> getCustomPropertiesList() {
        return customPropertiesList;
    }

    public void getPermissionsByAdElementId(AsyncQuery<List<Permission>> aQuery, Guid userId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetPermissionsByAdElementId,
                new IdQueryParameters(userId),
                aQuery);
    }

    public void getRoleActionGroupsByRoleId(AsyncQuery<List<ActionGroup>> aQuery, Guid roleId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetRoleActionGroupsByRoleId,
                new IdQueryParameters(roleId),
                aQuery);
    }

    public void isTemplateNameUnique(AsyncQuery<Boolean> aQuery, String templateName, Guid datacenterId) {
        aQuery.converterCallback = new Converter<Boolean>() {
            @Override
            public Boolean convert(Object source) {
                return source != null && !(Boolean) source;
            }
        };
        NameQueryParameters params = new NameQueryParameters(templateName);
        params.setDatacenterId(datacenterId);
        Frontend.getInstance().runQuery(VdcQueryType.IsVmTemlateWithSameNameExist,
                params,
                aQuery);
    }

    public void isVmNameUnique(AsyncQuery<Boolean> aQuery, String name, Guid datacenterId) {
        aQuery.converterCallback = new Converter<Boolean>() {
            @Override
            public Boolean convert(Object source) {
                return source != null && !(Boolean) source;
            }
        };
        NameQueryParameters params = new NameQueryParameters(name);
        params.setDatacenterId(datacenterId);
        Frontend.getInstance().runQuery(VdcQueryType.IsVmWithSameNameExist, params, aQuery);
    }

    public void getDataCentersWithPermittedActionOnClusters(AsyncQuery<List<StoragePool>> aQuery, ActionGroup actionGroup,
            final boolean supportsVirtService, final boolean supportsGlusterService) {
        aQuery.converterCallback = new ListConverter<>();

        GetDataCentersWithPermittedActionOnClustersParameters getDataCentersWithPermittedActionOnClustersParameters =
                new GetDataCentersWithPermittedActionOnClustersParameters();
        getDataCentersWithPermittedActionOnClustersParameters.setActionGroup(actionGroup);
        getDataCentersWithPermittedActionOnClustersParameters.setSupportsVirtService(supportsVirtService);
        getDataCentersWithPermittedActionOnClustersParameters.setSupportsGlusterService(supportsGlusterService);

        Frontend.getInstance().runQuery(VdcQueryType.GetDataCentersWithPermittedActionOnClusters,
                getDataCentersWithPermittedActionOnClustersParameters,
                aQuery);
    }

    public void getClustersWithPermittedAction(AsyncQuery<List<Cluster>> aQuery, ActionGroup actionGroup,
            final boolean supportsVirtService, final boolean supportsGlusterService) {
        aQuery.converterCallback = new Converter<List<Cluster>>() {
            @Override
            public List<Cluster> convert(Object source) {
                if (source != null) {
                    ArrayList<Cluster> list = (ArrayList<Cluster>) source;
                    return getClusterByServiceList(list, supportsVirtService, supportsGlusterService);
                }
                return new ArrayList<>();
            }
        };

        GetEntitiesWithPermittedActionParameters getEntitiesWithPermittedActionParameters =
                new GetEntitiesWithPermittedActionParameters();
        getEntitiesWithPermittedActionParameters.setActionGroup(actionGroup);
        Frontend.getInstance().runQuery(VdcQueryType.GetClustersWithPermittedAction, getEntitiesWithPermittedActionParameters, aQuery);
    }

    public void getClustersHavingHosts(AsyncQuery<List<Cluster>> aQuery) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllClustersHavingHosts,
                new VdcQueryParametersBase(),
                aQuery);
    }

    public void getAllVmTemplates(AsyncQuery<List<VmTemplate>> aQuery, final boolean refresh) {
        aQuery.converterCallback = new TemplateConverter();
        VdcQueryParametersBase params = new VdcQueryParametersBase();
        params.setRefresh(refresh);
        Frontend.getInstance().runQuery(VdcQueryType.GetAllVmTemplates, params, aQuery);
    }

    public void getStorageConnectionById(AsyncQuery<StorageServerConnections> aQuery, String id, boolean isRefresh) {
        aQuery.converterCallback = new Converter<StorageServerConnections>() {
            @Override
            public StorageServerConnections convert(Object source) {
                return source != null ? (StorageServerConnections) source : null;
            }
        };
        StorageServerConnectionQueryParametersBase params = new StorageServerConnectionQueryParametersBase(id);
        params.setRefresh(isRefresh);
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageServerConnectionById, params, aQuery);
    }

    public void getDataCentersByStorageDomain(AsyncQuery<List<StoragePool>> aQuery, Guid storageDomainId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetStoragePoolsByStorageDomainId,
                new IdQueryParameters(storageDomainId),
                aQuery);
    }

    public void getDataCenterVersions(AsyncQuery<List<Version>> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new SortListConverter<>();
        IdQueryParameters tempVar = new IdQueryParameters(dataCenterId);
        Frontend.getInstance().runQuery(VdcQueryType.GetAvailableClusterVersionsByStoragePool, tempVar, aQuery);
    }

    public void getDataCenterMaxNameLength(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.StoragePoolNameSizeLimit,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getClusterServerMemoryOverCommit(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(0);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.MaxVdsMemOverCommitForServers,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getClusterDesktopMemoryOverCommit(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(0);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.MaxVdsMemOverCommit,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getAllowClusterWithVirtGlusterEnabled(AsyncQuery<Boolean> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(Boolean.TRUE);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.AllowClusterWithVirtGlusterEnabled,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getCPUList(AsyncQuery<List<ServerCpu>> aQuery, Version version) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllServerCpuList,
                new GetAllServerCpuListParameters(version),
                aQuery);
    }

    public void getPmTypeList(AsyncQuery<List<String>> aQuery, Version version) {
        aQuery.converterCallback = new Converter<List<String>>() {
            @Override
            public List<String> convert(Object source) {
                ArrayList<String> list = new ArrayList<>();
                if (source != null) {
                    String[] array = ((String) source).split("[,]", -1); //$NON-NLS-1$
                    for (String item : array) {
                        list.add(item);
                    }
                }
                return list;
            }
        };
        GetConfigurationValueParameters param = new GetConfigurationValueParameters(ConfigurationValues.VdsFenceType);
        param.setVersion(version != null ? version.toString() : getDefaultConfigurationVersion());
        Frontend.getInstance().runQuery(VdcQueryType.GetFenceConfigurationValue, param, aQuery);
    }

    public void getPmOptions(AsyncQuery<List<String>> aQuery, final String pmType, String version) {
        aQuery.converterCallback = new Converter<List<String>>() {
            @Override
            public List<String> convert(Object source) {
                HashMap<String, ArrayList<String>> cachedPmMap = new HashMap<>();
                HashMap<String, HashMap<String, Object>> dict =
                        (HashMap<String, HashMap<String, Object>>) source;
                for (Map.Entry<String, HashMap<String, Object>> pair : dict.entrySet()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Map.Entry<String, Object> p : pair.getValue().entrySet()) {
                        list.add(p.getKey());
                    }

                    cachedPmMap.put(pair.getKey(), list);
                }
                return cachedPmMap.get(pmType);
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetAgentFenceOptions, new GetAgentFenceOptionsQueryParameters(version), aQuery);
    }

    public void getNetworkList(AsyncQuery<List<Network>> aQuery, Guid dataCenterId) {
        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetAllNetworks, new IdQueryParameters(dataCenterId), aQuery);
    }

    public void getISOStorageDomainList(AsyncQuery<List<StorageDomain>> aQuery) {
        aQuery.converterCallback = new Converter<List<StorageDomain>>() {
            @Override
            public List<StorageDomain> convert(Object source) {
                if (source != null) {
                    ArrayList<StorageDomain> allStorageDomains =
                            (ArrayList<StorageDomain>) source;
                    ArrayList<StorageDomain> isoStorageDomains = new ArrayList<>();
                    for (StorageDomain storageDomain : allStorageDomains) {
                        if (storageDomain.getStorageDomainType() == StorageDomainType.ISO) {
                            isoStorageDomains.add(storageDomain);
                        }
                    }
                    return isoStorageDomains;
                }
                return new ArrayList<>();
            }
        };

        SearchParameters searchParams = new SearchParameters("Storage:", SearchType.StorageDomain); //$NON-NLS-1$
        searchParams.setMaxCount(9999);

        Frontend.getInstance().runQuery(VdcQueryType.Search, searchParams, aQuery);
    }

    public void getStorageDomainList(AsyncQuery<List<StorageDomain>> aQuery) {
        aQuery.converterCallback = new ListConverter<>();

        SearchParameters searchParams = new SearchParameters("Storage:", SearchType.StorageDomain); //$NON-NLS-1$
        searchParams.setMaxCount(9999);

        Frontend.getInstance().runQuery(VdcQueryType.Search, searchParams, aQuery);
    }

    public void getLocalStorageHost(AsyncQuery<VDS> aQuery, String dataCenterName) {
        aQuery.converterCallback = new GetFirstConverter<>();
        SearchParameters sp = new SearchParameters("hosts: datacenter=" + dataCenterName, SearchType.VDS); //$NON-NLS-1$
        Frontend.getInstance().runQuery(VdcQueryType.Search, sp, aQuery);
    }

    public void getStorageDomainsByConnection(AsyncQuery<List<StorageDomain>> aQuery, Guid storagePoolId, String connectionPath) {
        aQuery.converterCallback = new CastingConverter<>();

        GetStorageDomainsByConnectionParameters param = new GetStorageDomainsByConnectionParameters();
        param.setConnection(connectionPath);
        if (storagePoolId != null) {
            param.setStoragePoolId(storagePoolId);
        }

        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainsByConnection, param, aQuery);
    }

    public void getExistingStorageDomainList(AsyncQuery<List<StorageDomain>> aQuery,
            Guid hostId,
            StorageDomainType domainType,
            StorageType storageType,
            String path) {
        aQuery.converterCallback = new CastingConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetExistingStorageDomainList, new GetExistingStorageDomainListParameters(hostId,
                storageType,
                domainType,
                path), aQuery);
    }

    public void getStorageDomainMaxNameLength(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(1);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.StorageDomainNameSizeLimit,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void isStorageDomainNameUnique(AsyncQuery<Boolean> aQuery, String name) {
        aQuery.converterCallback = new Converter<Boolean>() {
            @Override
            public Boolean convert(Object source) {
                if (source != null) {
                    ArrayList<StorageDomain> storageDomains = (ArrayList<StorageDomain>) source;
                    return storageDomains.isEmpty();
                }

                return false;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.Search, new SearchParameters("Storage: name=" + name, //$NON-NLS-1$
                SearchType.StorageDomain), aQuery);
    }

    public void getNetworkConnectivityCheckTimeoutInSeconds(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(120);
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.NetworkConnectivityCheckTimeoutInSeconds,
                        getDefaultConfigurationVersion()),
                aQuery);
    }

    public void getMaxSpmPriority(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(0);
        aQuery.getAsyncCallback().onSuccess(10);
    }

    public void getDefaultSpmPriority(AsyncQuery<Integer> aQuery) {
        aQuery.converterCallback = new DefaultValueConverter<>(0);
        aQuery.getAsyncCallback().onSuccess(5);
    }

    public void getDefaultPmProxyPreferences(AsyncQuery<String> query) {
        getConfigFromCache(
                new GetConfigurationValueParameters(ConfigurationValues.FenceProxyDefaultPreferences,
                        getDefaultConfigurationVersion()),
                query);
    }

    public void getRootTag(AsyncQuery<Tags> aQuery) {
        aQuery.converterCallback = new Converter<Tags>() {
            @Override
            public Tags convert(Object source) {
                if (source != null) {
                    Tags tag = (Tags) source;

                    Tags root =
                            new Tags(tag.getDescription(),
                                    tag.getParentId(),
                                    tag.getIsReadonly(),
                                    tag.getTagId(),
                                    tag.getTagName());
                    if (tag.getChildren() != null) {
                        fillTagsRecursive(root, tag.getChildren());
                    }

                    return root;
                }

                return new Tags();
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetRootTag, new VdcQueryParametersBase(), aQuery);
    }

    private void setAttachedTagsConverter(AsyncQuery<List<Tags>> aQuery) {
        aQuery.converterCallback = new Converter<List<Tags>>() {
            @Override
            public List<Tags> convert(Object source) {
                if (source != null) {
                    ArrayList<Tags> ret = new ArrayList<>();
                    for (Tags tags : (ArrayList<Tags>) source) {
                        if (tags.getType() == TagsType.GeneralTag) {
                            ret.add(tags);
                        }
                    }
                    return ret;
                }

                return new ArrayList<>();
            }
        };
    }

    public void getAttachedTagsToVm(AsyncQuery<List<Tags>> aQuery, Guid id) {
        setAttachedTagsConverter(aQuery);

        Frontend.getInstance().runQuery(VdcQueryType.GetTagsByVmId, new GetTagsByVmIdParameters(id.toString()), aQuery);
    }

    public void getAttachedTagsToUser(AsyncQuery<List<Tags>> aQuery, Guid id) {
        setAttachedTagsConverter(aQuery);

        Frontend.getInstance().runQuery(VdcQueryType.GetTagsByUserId, new GetTagsByUserIdParameters(id.toString()), aQuery);
    }

    public void getAttachedTagsToUserGroup(AsyncQuery<List<Tags>> aQuery, Guid id) {
        setAttachedTagsConverter(aQuery);

        Frontend.getInstance().runQuery(VdcQueryType.GetTagsByUserGroupId, new GetTagsByUserGroupIdParameters(id.toString()), aQuery);
    }

    public void getAttachedTagsToHost(AsyncQuery<List<Tags>> aQuery, Guid id) {
        setAttachedTagsConverter(aQuery);

        Frontend.getInstance().runQuery(VdcQueryType.GetTagsByVdsId, new GetTagsByVdsIdParameters(id.toString()), aQuery);
    }

    public void getoVirtISOsList(AsyncQuery<List<RpmVersion>> aQuery, Guid id) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetoVirtISOs, new IdQueryParameters(id), aQuery);
    }

    public void getLunsByVgId(AsyncQuery<List<LUNs>> aQuery, String vgId, Guid vdsId) {
        aQuery.converterCallback = new ListConverter<>();
        GetLunsByVgIdParameters params = new GetLunsByVgIdParameters(vgId, vdsId);
        Frontend.getInstance().runQuery(VdcQueryType.GetLunsByVgId, params, aQuery);
    }

    public void getAllTemplatesFromExportDomain(AsyncQuery<Map<VmTemplate, ArrayList<DiskImage>>> aQuery, Guid storagePoolId, Guid storageDomainId) {
        aQuery.converterCallback = new MapConverter<>();
        GetAllFromExportDomainQueryParameters getAllFromExportDomainQueryParamenters =
                new GetAllFromExportDomainQueryParameters(storagePoolId, storageDomainId);
        Frontend.getInstance().runQuery(VdcQueryType.GetTemplatesFromExportDomain, getAllFromExportDomainQueryParamenters, aQuery);
    }

    public void getUpHostListByCluster(AsyncQuery<List<VDS>> aQuery, String clusterName) {
        aQuery.converterCallback = new ListConverter<>();
        getUpHostListByCluster(aQuery, clusterName, null);
    }

    public void getUpHostListByCluster(AsyncQuery aQuery, String clusterName, Integer maxCount) {
        SearchParameters searchParameters =
                new SearchParameters("Host: cluster = " + clusterName + " and status = up", SearchType.VDS); //$NON-NLS-1$ //$NON-NLS-2$
        if (maxCount != null) {
            searchParameters.setMaxCount(maxCount);
        }
        Frontend.getInstance().runQuery(VdcQueryType.Search, searchParameters, aQuery);
    }

    public void getVmNicList(AsyncQuery<List<VmNetworkInterface>> aQuery, Guid id) {
        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetVmInterfacesByVmId, new IdQueryParameters(id), aQuery);
    }

    public void getTemplateNicList(AsyncQuery<List<VmNetworkInterface>> aQuery, Guid id) {
        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetTemplateInterfacesByTemplateId, new IdQueryParameters(id), aQuery);
    }

    public void getVmSnapshotList(AsyncQuery<List<Snapshot>> aQuery, Guid id) {
        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetAllVmSnapshotsByVmId, new IdQueryParameters(id), aQuery);
    }

    public void getVmsRunningOnOrMigratingToVds(AsyncQuery<List<VM>> aQuery, Guid id) {
        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetVmsRunningOnOrMigratingToVds,
                new IdQueryParameters(id),
                aQuery);
    }

    public void getVmsFromExternalServer(AsyncQuery aQuery, Guid dataCenterId, Guid vdsId,
            String url, String username, String password, OriginType originType, List<String> vmsToImport) {

        aQuery.converterCallback = new ListConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetVmsFromExternalProvider,
                new GetVmsFromExternalProviderQueryParameters(url, username, password, originType, vdsId, dataCenterId, vmsToImport),
                aQuery);
    }

    public void getVmFromOva(AsyncQuery<VdcQueryReturnValue> aQuery, Guid vdsId, String path) {
        aQuery.setHandleFailure(true);
        Frontend.getInstance().runQuery(
                VdcQueryType.GetVmFromOva,
                new GetVmFromOvaQueryParameters(vdsId, path),
                aQuery);
    }

    public void getVmDiskList(AsyncQuery<List<Disk>> aQuery, Guid id) {
        aQuery.converterCallback = new Converter<List<Disk>>() {
            @Override
            public List<Disk> convert(Object source) {
                ArrayList<Disk> list = new ArrayList<>();
                if (source != null) {
                    Iterable listEnumerable = (Iterable) source;
                    Iterator listIterator = listEnumerable.iterator();
                    while (listIterator.hasNext()) {
                        list.add((Disk) listIterator.next());
                    }
                }
                return list;
            }
        };

        Frontend.getInstance().runQuery(VdcQueryType.GetAllDisksByVmId, new IdQueryParameters(id).withoutRefresh(), aQuery);
    }

    public void getVmListByClusterName(AsyncQuery<List<VM>> aQuery, String clusterName) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.Search,
                new SearchParameters("Vms: cluster=" + clusterName, SearchType.VM), aQuery); //$NON-NLS-1$
    }

    public void getDiskList(AsyncQuery<List<DiskImage>> aQuery) {
        aQuery.converterCallback = new ListConverter<>();

        SearchParameters searchParams = new SearchParameters("Disks:", SearchType.Disk); //$NON-NLS-1$
        searchParams.setMaxCount(9999);

        Frontend.getInstance().runQuery(VdcQueryType.Search, searchParams, aQuery);
    }

    public void getNextAvailableDiskAliasNameByVMId(AsyncQuery<String> aQuery, Guid vmId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetNextAvailableDiskAliasNameByVMId,
                new IdQueryParameters(vmId),
                aQuery);
    }

    public void isPoolNameUnique(AsyncQuery<Boolean> aQuery, String name) {

        aQuery.converterCallback = new Converter<Boolean>() {
            @Override
            public Boolean convert(Object source) {
                if (source != null) {
                    return !(Boolean) source;
                }

                return false;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.IsVmPoolWithSameNameExists,
                new NameQueryParameters(name),
                aQuery);
    }

    public void getVmConfigurationBySnapshot(AsyncQuery<VM> aQuery, Guid snapshotSourceId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmConfigurationBySnapshot,
                new IdQueryParameters(snapshotSourceId).withoutRefresh(),
                aQuery);
    }

    public void getAllAttachableDisks(AsyncQuery<List<Disk>> aQuery, Guid storagePoolId, Guid vmId) {
        aQuery.converterCallback = new ListConverter<>();
        GetAllAttachableDisksForVmQueryParameters params = new GetAllAttachableDisksForVmQueryParameters(storagePoolId);
        params.setVmId(vmId);
        Frontend.getInstance().runQuery(VdcQueryType.GetAllAttachableDisksForVm, params, aQuery);
    }

    public void getAncestorImagesByImagesIds(AsyncQuery<Map<Guid, DiskImage>> aQuery, List<Guid> imagesIds) {
        aQuery.converterCallback = new CastingConverter<>();
        IdsQueryParameters params = new IdsQueryParameters(imagesIds);
        Frontend.getInstance().runQuery(VdcQueryType.GetAncestorImagesByImagesIds, params, aQuery);
    }

    public void getPermittedStorageDomainsByStoragePoolId(AsyncQuery<List<StorageDomain>> aQuery,
            Guid dataCenterId,
            ActionGroup actionGroup) {
        aQuery.converterCallback = new ListConverter<>();
        GetPermittedStorageDomainsByStoragePoolIdParameters params =
                new GetPermittedStorageDomainsByStoragePoolIdParameters();

        params.setStoragePoolId(dataCenterId);
        params.setActionGroup(actionGroup);

        Frontend.getInstance().runQuery(VdcQueryType.GetPermittedStorageDomainsByStoragePoolId, params, aQuery);
    }

    public void getStorageDomainDefaultWipeAfterDelete(AsyncQuery<Boolean> aQuery, StorageType storageType) {
        aQuery.converterCallback = new CastingConverter<>();
        GetStorageDomainDefaultWipeAfterDeleteParameters params =
                new GetStorageDomainDefaultWipeAfterDeleteParameters(storageType);
        Frontend.getInstance().runQuery(VdcQueryType.GetStorageDomainDefaultWipeAfterDelete, params, aQuery);
    }

    public void getAllDataCenterNetworks(AsyncQuery<List<Network>> aQuery, Guid storagePoolId) {
        aQuery.converterCallback = new ListConverter<>();
        IdQueryParameters params = new IdQueryParameters(storagePoolId);
        Frontend.getInstance().runQuery(VdcQueryType.GetNetworksByDataCenterId, params, aQuery);
    }

    public void getManagementNetworkCandidates(AsyncQuery<List<Network>> aQuery, Guid storagePoolId) {
        aQuery.converterCallback = new ListConverter<>();
        IdQueryParameters params = new IdQueryParameters(storagePoolId);
        Frontend.getInstance().runQuery(VdcQueryType.GetManagementNetworkCandidates, params, aQuery);
    }

    public void getStorageConnectionsByDataCenterIdAndStorageType(AsyncQuery<List<StorageServerConnections>> aQuery,
                                                                         Guid storagePoolId,
                                                                         StorageType storageType) {
        aQuery.converterCallback = new CastingConverter<>();
        GetConnectionsByDataCenterAndStorageTypeParameters params = new GetConnectionsByDataCenterAndStorageTypeParameters(storagePoolId, storageType);
        Frontend.getInstance().runQuery(VdcQueryType.GetConnectionsByDataCenterAndStorageType, params, aQuery);
    }

    /**
     * Cache configuration values [raw (not converted) values from vdc_options table].
     */
    private void cacheConfigValues(AsyncQuery<Map<KeyValuePairCompat<ConfigurationValues, String>, Object>> aQuery) {
        aQuery.converterCallback = new Converter<Map<KeyValuePairCompat<ConfigurationValues, String>, Object>>() {
            @Override
            public Map<KeyValuePairCompat<ConfigurationValues, String>, Object> convert(Object returnValue) {
                if (returnValue != null) {
                    cachedConfigValuesPreConvert.putAll((HashMap<KeyValuePairCompat<ConfigurationValues, String>, Object>) returnValue);
                }
                return cachedConfigValuesPreConvert;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetConfigurationValues, new VdcQueryParametersBase(), aQuery);
    }

    /**
     * Get configuration value from 'cachedConfigValuesPreConvert' (raw values from vdc_options table).
     */
    public Object getConfigValuePreConverted(ConfigurationValues configValue, String version) {
        KeyValuePairCompat<ConfigurationValues, String> key = new KeyValuePairCompat<>(configValue, version);

        return cachedConfigValuesPreConvert.get(key);
    }

    /**
     * Get configuration value from 'cachedConfigValuesPreConvert' (raw values from vdc_options table).
     */
    public Object getConfigValuePreConverted(ConfigurationValues configValue) {
        KeyValuePairCompat<ConfigurationValues, String> key =
                new KeyValuePairCompat<>(configValue, getDefaultConfigurationVersion());

        return cachedConfigValuesPreConvert.get(key);
    }

    /**
     * method to get an item from config while caching it (config is not supposed to change during a session)
     *
     * @param aQuery
     *            an async query
     * @param parameters
     *            a converter for the async query
     */
    public <T> void getConfigFromCache(GetConfigurationValueParameters parameters, AsyncQuery<T> aQuery) {
        // cache key
        final KeyValuePairCompat<ConfigurationValues, String> config_key =
                new KeyValuePairCompat<>(parameters.getConfigValue(), parameters.getVersion());

        T returnValue = null;

        if (cachedConfigValues.containsKey(config_key)) {
            // cache hit
            returnValue = (T) cachedConfigValues.get(config_key);
        }
        // cache miss: convert configuration value using query's converter
        // and call asyncCallback's onSuccess
        else if (cachedConfigValuesPreConvert.containsKey(config_key)) {
            returnValue = (T) cachedConfigValuesPreConvert.get(config_key);

            // run converter
            if (aQuery.converterCallback != null) {
                returnValue = aQuery.converterCallback.convert(returnValue);
            }
            if (returnValue != null) {
                cachedConfigValues.put(config_key, returnValue);
            }
        }
        aQuery.getAsyncCallback().onSuccess(returnValue);
    }

    public ArrayList<QuotaEnforcementTypeEnum> getQuotaEnforcmentTypes() {
        return new ArrayList<>(Arrays.asList(new QuotaEnforcementTypeEnum[]{
                QuotaEnforcementTypeEnum.DISABLED,
                QuotaEnforcementTypeEnum.SOFT_ENFORCEMENT,
                QuotaEnforcementTypeEnum.HARD_ENFORCEMENT }));
    }

    private static class TemplateConverter implements Converter<List<VmTemplate>> {

        @Override
        public List<VmTemplate> convert(Object source) {
            List<VmTemplate> list = new ArrayList<>();
            if (source != null) {
                VmTemplate blankTemplate = null;
                for (VmTemplate template : (List<VmTemplate>) source) {
                    if (template.getId().equals(Guid.Empty)) {
                        blankTemplate = template;
                    } else if (template.getStatus() == VmTemplateStatus.OK) {
                        list.add(template);
                    }
                }

                Collections.sort(list, new NameableComparator());
                if (blankTemplate != null) {
                    list.add(0, blankTemplate);
                }
            }

            return list;
        }
    }

    public void getExternalProviderHostList(AsyncQuery<List<VDS>> aQuery,
                                                   Guid providerId,
                                                   boolean filterOutExistingHosts,
                                                   String searchFilter) {
        aQuery.converterCallback = new ListConverter<>();
        GetHostListFromExternalProviderParameters params = new GetHostListFromExternalProviderParameters();
        params.setFilterOutExistingHosts(filterOutExistingHosts);
        params.setProviderId(providerId);
        params.setSearchFilter(searchFilter);
        Frontend.getInstance().runQuery(VdcQueryType.GetHostListFromExternalProvider,
                params,
                aQuery);
    }

    public void getExternalProviderDiscoveredHostList(AsyncQuery<List<ExternalDiscoveredHost>> aQuery, Provider provider) {
        aQuery.converterCallback = new ListConverter<>();
        ProviderQueryParameters params = new ProviderQueryParameters();
        params.setProvider(provider);
        Frontend.getInstance().runQuery(VdcQueryType.GetDiscoveredHostListFromExternalProvider, params, aQuery);
    }

    public void getExternalProviderHostGroupList(AsyncQuery<List<ExternalHostGroup>> aQuery, Provider provider) {
        aQuery.converterCallback = new ListConverter<>();

        ProviderQueryParameters params = new ProviderQueryParameters();
        params.setProvider(provider);
        Frontend.getInstance().runQuery(VdcQueryType.GetHostGroupsFromExternalProvider, params, aQuery);
    }

    public void getExternalProviderComputeResourceList(AsyncQuery<List<ExternalComputeResource>> aQuery, Provider provider) {
        aQuery.converterCallback = new ListConverter<>();

        ProviderQueryParameters params = new ProviderQueryParameters();
        params.setProvider(provider);
        Frontend.getInstance().runQuery(VdcQueryType.GetComputeResourceFromExternalProvider, params, aQuery);
    }

    public void getAllProviders(AsyncQuery<List<Provider<?>>> aQuery, boolean doRefresh) {
        aQuery.converterCallback = new SortListByNameConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllProviders, doRefresh ? new GetAllProvidersParameters() :
                new GetAllProvidersParameters().withoutRefresh(), aQuery);
    }

    public void getAllProvidersByProvidedEntity(AsyncQuery query, final VdcObjectType providedEntity) {
        query.converterCallback = new Converter<List<Provider>>() {
            @Override
            public List<Provider> convert(Object returnValue) {
                if (returnValue == null) {
                    return new ArrayList<>();
                }
                List<Provider> providers =
                        Linq.toList(Linq.filterProvidersByProvidedType((Collection<Provider>) returnValue, providedEntity));
                Collections.sort(providers, new NameableComparator());
                return providers;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.GetAllProviders, new GetAllProvidersParameters(), query);
    }

    public void getAllNetworkProviders(AsyncQuery<List<Provider<?>>> query) {
        getAllProvidersByProvidedEntity(query, VdcObjectType.Network);
    }

    public void getAllProvidersByType(AsyncQuery<List<Provider<?>>> aQuery, ProviderType providerType) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllProviders, new GetAllProvidersParameters(providerType), aQuery);
    }

    public void getProviderCertificateChain(AsyncQuery<List<CertificateInfo>> aQuery, Provider provider) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetProviderCertificateChain,
                new ProviderQueryParameters(provider),
                aQuery);
    }

    public void fillTagsRecursive(Tags tagToFill, List<Tags> children) {
        ArrayList<Tags> list = new ArrayList<>();

        for (Tags tag : children) {
            // tags child = new tags(tag.description, tag.parent_id, tag.IsReadonly, tag.tag_id, tag.tag_name);
            if (tag.getType() == TagsType.GeneralTag) {
                list.add(tag);
                if (tag.getChildren() != null) {
                    fillTagsRecursive(tag, tag.getChildren());
                }
            }

        }

        tagToFill.setChildren(list);
    }

    public ArrayList<EventNotificationEntity> getEventNotificationTypeList() {
        ArrayList<EventNotificationEntity> ret = new ArrayList<>();
        // TODO: We can translate it here too
        for (EventNotificationEntity entity : EventNotificationEntity.values()) {
            if (entity != EventNotificationEntity.UNKNOWN) {
                ret.add(entity);
            }
        }
        return ret;
    }

    public Map<EventNotificationEntity, HashSet<AuditLogType>> getAvailableNotificationEvents() {
        return VdcEventNotificationUtils.getNotificationEvents();
    }

    public void getNicTypeList(final int osId, Version version, AsyncQuery<List<VmInterfaceType>> asyncQuery) {
        asyncQuery.converterCallback = new Converter<List<VmInterfaceType>>() {
            @Override
            public List<VmInterfaceType> convert(Object returnValue) {
                ArrayList<String> nics = (ArrayList<String>) returnValue;
                List<VmInterfaceType> interfaceTypes = new ArrayList<>();
                for (String nic : nics) {
                    try {
                        interfaceTypes.add(VmInterfaceType.valueOf(nic));
                    } catch (IllegalArgumentException e) {
                        // ignore if we can't find the enum value.
                    }
                }
                return interfaceTypes;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetNetworkDevices, osId, version),
                asyncQuery);
    }

    public void getIsPasswordDelegationPossible(AsyncQuery<Boolean> asyncQuery) {
        asyncQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.IsPasswordDelegationPossible,
                new VdcQueryParametersBase(),
                asyncQuery);
    }

    public VmInterfaceType getDefaultNicType(Collection<VmInterfaceType> items) {
        if (items == null || items.isEmpty()) {
            return null;
        } else if (items.contains(VmInterfaceType.pv)) {
            return VmInterfaceType.pv;
        } else {
            return items.iterator().next();
        }
    }

    public int getClusterDefaultMemoryOverCommit() {
        return 100;
    }

    public boolean getClusterDefaultCountThreadsAsCores() {
        return false;
    }

    public ArrayList<VolumeType> getVolumeTypeList() {
        return new ArrayList<>(Arrays.asList(VolumeType.Preallocated, VolumeType.Sparse));
    }

    public ArrayList<VolumeFormat> getVolumeFormats() {
        return new ArrayList<>(Arrays.asList(VolumeFormat.COW, VolumeFormat.RAW));
    }

    public ArrayList<StorageType> getStorageTypeList() {
        return new ArrayList<>(Arrays.asList(StorageType.ISCSI, StorageType.FCP));
    }

    public void getDiskInterfaceList(int osId, Version clusterVersion, AsyncQuery<List<DiskInterface>> asyncQuery) {
        asyncQuery.converterCallback = new Converter<List<DiskInterface>>() {
            @Override
            public List<DiskInterface> convert(Object returnValue) {
                ArrayList<String> interfaces = (ArrayList<String>) returnValue;
                List<DiskInterface> interfaceTypes = new ArrayList<>();
                for (String diskIfs : interfaces) {
                    try {
                        interfaceTypes.add(DiskInterface.valueOf(diskIfs));
                    } catch (IllegalArgumentException e) {
                        // ignore if we can't find the enum value.
                    }
                }
                return interfaceTypes;
            }
        };
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetDiskInterfaces, osId, clusterVersion),
                asyncQuery);
    }

    public ArrayList<DiskInterface> getDiskInterfaceList() {
        ArrayList<DiskInterface> diskInterfaces = new ArrayList<>(
                Arrays.asList(new DiskInterface[]{
                        DiskInterface.IDE,
                        DiskInterface.VirtIO,
                        DiskInterface.VirtIO_SCSI,
                        DiskInterface.SPAPR_VSCSI
                }));

        return diskInterfaces;
    }

    public String getNewNicName(Collection<VmNetworkInterface> existingInterfaces) {
        int maxIfaceNumber = 0;
        if (existingInterfaces != null) {
            for (VmNetworkInterface iface : existingInterfaces) {
                // name of Interface is "eth<n>" (<n>: integer).
                if (iface.getName().length() > 3) {
                    final Integer ifaceNumber = IntegerCompat.tryParse(iface.getName().substring(3));
                    if (ifaceNumber != null && ifaceNumber > maxIfaceNumber) {
                        maxIfaceNumber = ifaceNumber;
                    }
                }
            }
        }

        return "nic" + (maxIfaceNumber + 1); //$NON-NLS-1$
    }

    public Guid getEntityGuid(Object entity) {
        if (entity instanceof BusinessEntity) {
            //BusinessEntity can have lot of different ID types, but from this context it cannot be determined.
            Object id = ((BusinessEntity<?>) entity).getId();

            //check whether result can be casted to Guid, otherwise continue with explicit rules.
            if (id instanceof Guid) {
                return (Guid) id;
            }
        }

        if (entity instanceof VmPool) {
            return ((VmPool) entity).getVmPoolId();
        } else if (entity instanceof DbUser) {
            return ((DbUser) entity).getId();
        } else if (entity instanceof DbGroup) {
            return ((DbGroup) entity).getId();
        } else {
            return Guid.Empty;
        }
    }

    public boolean isWindowsOsType(Integer osType) {
        // can be null as a consequence of setItems on ListModel
        if (osType == null) {
            return false;
        }

        return windowsOsIds.contains(osType);
    }

    public boolean isLinuxOsType(Integer osId) {
        // can be null as a consequence of setItems on ListModel
        if (osId == null) {
            return false;
        }

        return linuxOsIds.contains(osId);
    }

    public void initWindowsOsTypes() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetWindowsOss),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        windowsOsIds = (ArrayList<Integer>) returnValue.getReturnValue();
                    }
                }));
    }

    public void initLinuxOsTypes() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetLinuxOss),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        linuxOsIds = (ArrayList<Integer>) returnValue.getReturnValue();
                    }
                }));
    }

    public void initUniqueOsNames() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetUniqueOsNames),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        uniqueOsNames = returnValue.getReturnValue();
                        // Initialize specific UI dependencies for search
                        SimpleDependencyInjector.getInstance().bind(new OsValueAutoCompleter(uniqueOsNames));
                    }
                }));
    }

    public void initOsNames() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetOsNames),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        osNames = returnValue.getReturnValue();
                        initOsIds();
                    }
                }));
    }

    private void initOsDefaultIconIds() {
        Frontend.getInstance().runQuery(VdcQueryType.GetVmIconDefaults, new VdcQueryParametersBase(),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        final Map<Integer, VmIconIdSizePair> returnMap = returnValue.getReturnValue();
                        if (returnMap.get(DEFAULT_OS_ID) == null) {
                            throw new RuntimeException("Engine did not provide icon IDs of default OS."); //$NON-NLS-1$
                        }
                        osIdToDefaultIconIdMap = Collections.unmodifiableMap(returnMap);
                        initializeLargeToSmallIconMap();
                    }
                }));
    }

    private void initializeLargeToSmallIconMap() {
        largeToSmallOsDefaultIconIdMap = new HashMap<>();
        for (VmIconIdSizePair pair : osIdToDefaultIconIdMap.values()) {
            largeToSmallOsDefaultIconIdMap.put(pair.getLarge(), pair.getSmall());
        }
    }

    private void initOsIds() {
        osIds = new ArrayList<>(osNames.keySet());
        Collections.sort(osIds, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return osNames.get(o1).compareTo(osNames.get(o2));
            }
        });
    }

    public void initOsArchitecture() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetOsArchitectures),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        osArchitectures = returnValue.getReturnValue();
                    }
                }));
    }

    public boolean osNameExists(Integer osId) {
        return osNames.keySet().contains(osId);
    }

    public String getOsName(Integer osId) {
        // can be null as a consequence of setItems on ListModel
        if (osId == null) {
            return "";
        }

        return osNames.get(osId);
    }

    public Guid getOsDefaultIconId(Integer osId, boolean small) {
        if (osId == null) {
            return getDefaultIconId(small);
        }
        final VmIconIdSizePair pair = osIdToDefaultIconIdMap.get(osId);
        if (pair != null) {
            return pair.get(small);
        }
        return getDefaultIconId(small);
    }

    public boolean isCustomIconId(Guid iconId) {
        return !largeToSmallOsDefaultIconIdMap.containsKey(iconId)
                && !largeToSmallOsDefaultIconIdMap.containsValue(iconId);
    }

    public Guid getDefaultIconId(boolean small) {
        final VmIconIdSizePair pair = osIdToDefaultIconIdMap.get(DEFAULT_OS_ID);
        if (pair != null) {
            return pair.get(small);
        }
        throw new RuntimeException("Icon of default operating system not found."); //$NON-NLS-1$
    }

    public Guid getSmallByLargeOsDefaultIconId(Guid largeIconId) {
        return largeToSmallOsDefaultIconIdMap.get(largeIconId);
    }

    public boolean hasSpiceSupport(int osId, Version version) {
        for (Pair<GraphicsType, DisplayType> graphicsDisplayPair : getGraphicsAndDisplays(osId, version)) {
            if (graphicsDisplayPair.getFirst() == GraphicsType.SPICE) {
                return true;
            }
        }
        return false;
    }

    public List<Pair<GraphicsType, DisplayType>> getGraphicsAndDisplays(int osId, Version version) {
        return graphicsAndDisplays.get(osId).get(version);
    }

    private void initDisplayTypes() {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository,
                new OsQueryParameters(OsRepositoryVerb.GetDisplayTypes),
                new AsyncQuery<>(new AsyncCallback<VdcQueryReturnValue>() {
                    @Override
                    public void onSuccess(VdcQueryReturnValue returnValue) {
                        graphicsAndDisplays = returnValue.getReturnValue();
                    }
                }));
    }

    public List<Integer> getOsIds(ArchitectureType architectureType) {

        List<Integer> osIds = new ArrayList<>();

        for (Entry<Integer, ArchitectureType> entry : osArchitectures.entrySet()) {
            if (entry.getValue() == architectureType) {
                osIds.add(entry.getKey());
            }
        }

        Collections.sort(osIds, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return osNames.get(o1).compareTo(osNames.get(o2));
            }
        });

        return osIds;
    }

    public void getVmWatchdogTypes(int osId, Version version,
            AsyncQuery<VdcQueryReturnValue> asyncQuery) {
        Frontend.getInstance().runQuery(VdcQueryType.OsRepository, new OsQueryParameters(
                OsRepositoryVerb.GetVmWatchdogTypes, osId, version), asyncQuery);
    }

    public ArrayList<Map.Entry<String, EntityModel<String>>> getBondingOptionListDependingOnNetwork(
            RefObject<Map.Entry<String, EntityModel<String>>> defaultItem, boolean hasVmNetworkAttached) {
        ArrayList<Map.Entry<String, EntityModel<String>>> list = new ArrayList<>();

        for(BondMode mode : BondMode.values()){
            if (!mode.isBondModeValidForVmNetwork() && hasVmNetworkAttached){
                continue;
            }
            KeyValuePairCompat<String, EntityModel<String>> bondOption = getBondOption(mode);
            list.add(bondOption);
            if (mode.equals(BondMode.BOND4)){
                defaultItem.argvalue = bondOption;
            }
        }

        EntityModel<String> entityModel = new EntityModel<>();
        entityModel.setEntity(""); //$NON-NLS-1$
        list.add(new KeyValuePairCompat<>(SetupNetworksBondModel.CUSTOM_BONDING_MODE, entityModel));
        return list;
    }

    private KeyValuePairCompat<String, EntityModel<String>> getBondOption(BondMode mode){
        EntityModel<String> entityModel = new EntityModel<>();
        entityModel.setEntity(mode.getDescription());
        return new KeyValuePairCompat<>(mode.getConfigurationValue(), entityModel);
    }

    public int getMaxVmPriority() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.VmPriorityMaxValue,
                getDefaultConfigurationVersion());
    }

    public int roundPriority(int priority) {
        int max = getMaxVmPriority();
        int medium = max / 2;

        int[] levels = new int[] { 1, medium, max };

        for (int i = 0; i < levels.length; i++) {
            int lengthToLess = levels[i] - priority;
            int lengthToMore = levels[i + 1] - priority;

            if (lengthToMore < 0) {
                continue;
            }

            return Math.abs(lengthToLess) < lengthToMore ? levels[i] : levels[i + 1];
        }

        return 0;
    }

    public void getVmGuestAgentInterfacesByVmId(AsyncQuery<List<VmGuestAgentInterface>> aQuery, Guid vmId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmGuestAgentInterfacesByVmId,
                new IdQueryParameters(vmId).withoutRefresh(), aQuery);
    }

    public void getVnicProfilesByNetworkId(AsyncQuery<List<VnicProfileView>> aQuery, Guid networkId) {
        aQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVnicProfilesByNetworkId, new IdQueryParameters(networkId), aQuery);
    }

    public void getVnicProfilesByDcId(AsyncQuery<List<VnicProfileView>> aQuery, Guid dcId) {
        // do not replace a converter = just add if none provided
        if (aQuery.converterCallback == null) {
            aQuery.converterCallback = new ListConverter<>();
        }
        Frontend.getInstance().runQuery(VdcQueryType.GetVnicProfilesByDataCenterId, new IdQueryParameters(dcId), aQuery);
    }

    public void getNumberOfVmsInCluster(AsyncQuery aQuery, Guid clusterId) {
        Frontend.getInstance().runQuery(VdcQueryType.GetNumberOfVmsInClusterByClusterId, new IdQueryParameters(clusterId),
                aQuery);
    }

    public Integer getMaxIoThreadsPerVm() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.MaxIoThreadsPerVm);
    }

    public ArrayList<Cluster> getClusterByServiceList(List<Cluster> list,
            boolean supportsVirtService,
            boolean supportsGlusterService) {
        final ArrayList<Cluster> filteredList = new ArrayList<>();
        for (Cluster cluster : list) {
            if ((supportsVirtService && cluster.supportsVirtService())
                    || (supportsGlusterService && cluster.supportsGlusterService())) {
                filteredList.add(cluster);
            }
        }

        // sort by cluster name
        Collections.sort(filteredList, new NameableComparator());
        return filteredList;
    }

    public String priorityToString(int value) {
        int roundedPriority = roundPriority(value);

        if (roundedPriority == 1) {
            return ConstantsManager.getInstance().getConstants().vmLowPriority();
        }
        else if (roundedPriority == getMaxVmPriority() / 2) {
            return ConstantsManager.getInstance().getConstants().vmMediumPriority();
        }
        else if (roundedPriority == getMaxVmPriority()) {
            return ConstantsManager.getInstance().getConstants().vmHighPriority();
        }
        else {
            return ConstantsManager.getInstance().getConstants().vmUnknownPriority();
        }
    }

    public void getExternalNetworkMap(AsyncQuery<Map<Network, Set<Guid>>> aQuery, Guid providerId) {
        aQuery.converterCallback = new MapConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllExternalNetworksOnProvider,
                new IdQueryParameters(providerId),
                aQuery);
    }

    public Integer getMaxVmNameLength() {
        Integer maxVmNameLength = (Integer) getConfigValuePreConverted(ConfigurationValues.MaxVmNameLength);
        if (maxVmNameLength == null) {
            return 64;
        }
        return maxVmNameLength;
    }

    public Integer getMaxVmNameLengthSysprep() {
        Integer maxVmNameLengthSysprep = (Integer) getConfigValuePreConverted(ConfigurationValues.MaxVmNameLengthSysprep);
        if (maxVmNameLengthSysprep == null) {
            return 64;
        }
        return maxVmNameLengthSysprep;
    }

    public int getOptimizeSchedulerForSpeedPendingRequests() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.SpeedOptimizationSchedulingThreshold,
                getDefaultConfigurationVersion());
    }

    public boolean getScheudulingAllowOverbookingSupported() {
        return (Boolean) getConfigValuePreConverted(ConfigurationValues.SchedulerAllowOverBooking,
                getDefaultConfigurationVersion());
    }

    public int getSchedulerAllowOverbookingPendingRequestsThreshold() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.SchedulerOverBookingThreshold,
                getDefaultConfigurationVersion());
    }

    public Integer getDefaultOs (ArchitectureType architectureType) {
        return defaultOSes.get(architectureType);
    }

    public boolean isRebootCommandExecutionAllowed(List<VM> vms) {
        if (vms.isEmpty() || !VdcActionUtils.canExecutePartially(vms, VM.class, VdcActionType.RebootVm)) {
            return false;
        }

        for (VM vm : vms) {
            boolean guestAgentPresent = !StringHelper.isNullOrEmpty(vm.getIp());
            boolean acpiEnabled = Boolean.TRUE.equals(vm.getAcpiEnable());
            if (!(guestAgentPresent || acpiEnabled)) {
                return false;
            }
        }
        return true;
    }

    public boolean isMigrationPoliciesSupported(Version clusterVersion) {
        return (Boolean) getConfigValuePreConverted(ConfigurationValues.MigrationPoliciesSupported, clusterVersion.toString());
    }

    public List<String> getMigrationPoliciesSupportedVersions() {
        return getSupportedVersions(ConfigurationValues.MigrationPoliciesSupported);
    }

    private List<String> getSupportedVersions(ConfigurationValues option) {
        List<String> versions = new ArrayList<>();
        for (Entry<KeyValuePairCompat<ConfigurationValues, String>, Object> entry :
                cachedConfigValuesPreConvert.entrySet()) {
            if (entry.getKey().getKey() == option && (Boolean) entry.getValue()) {
                versions.add(entry.getKey().getValue());
            }
        }
        /* because if there is no special value for 'general' version in db then a record for 'general' is added with
         * value based on ConfigValues > @DefaultValueAttribute
         */
        if (versions.size() > 1 && versions.contains(GENERAL)) {
            versions.remove(GENERAL);
        }
        return versions;
    }

    public List<IStorageModel> getDataStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();
        models.addAll(getFileDataStorageModels());
        models.addAll(getBlockDataStorageModels());
        return models;
    }

    public List<IStorageModel> getFileDataStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();

        NfsStorageModel nfsDataModel = new NfsStorageModel();
        models.add(nfsDataModel);

        PosixStorageModel posixDataModel = new PosixStorageModel();
        models.add(posixDataModel);

        GlusterStorageModel GlusterDataModel = new GlusterStorageModel();
        models.add(GlusterDataModel);

        LocalStorageModel localDataModel = new LocalStorageModel();
        models.add(localDataModel);

        addTypeToStorageModels(StorageDomainType.Data, models);

        return models;
    }

    public List<IStorageModel> getBlockDataStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();

        IscsiStorageModel iscsiDataModel = new IscsiStorageModel();
        iscsiDataModel.setIsGrouppedByTarget(true);
        models.add(iscsiDataModel);

        FcpStorageModel fcpDataModel = new FcpStorageModel();
        models.add(fcpDataModel);

        addTypeToStorageModels(StorageDomainType.Data, models);

        return models;
    }

    public List<IStorageModel> getImportBlockDataStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();

        ImportIscsiStorageModel iscsiDataModel = new ImportIscsiStorageModel();
        models.add(iscsiDataModel);

        ImportFcpStorageModel fcpDataModel = new ImportFcpStorageModel();
        models.add(fcpDataModel);

        addTypeToStorageModels(StorageDomainType.Data, models);

        return models;
    }

    public List<IStorageModel> getIsoStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();

        NfsStorageModel nfsIsoModel = new NfsStorageModel();
        models.add(nfsIsoModel);

        PosixStorageModel posixIsoModel = new PosixStorageModel();
        models.add(posixIsoModel);

        GlusterStorageModel glusterStorageModel = new GlusterStorageModel();
        models.add(glusterStorageModel);

        LocalStorageModel localIsoModel = new LocalStorageModel();
        models.add(localIsoModel);

        addTypeToStorageModels(StorageDomainType.ISO, models);


        return models;
    }

    public List<IStorageModel> getExportStorageModels() {
        ArrayList<IStorageModel> models = new ArrayList<>();

        NfsStorageModel nfsExportModel = new NfsStorageModel();
        models.add(nfsExportModel);

        PosixStorageModel posixExportModel = new PosixStorageModel();
        models.add(posixExportModel);

        GlusterStorageModel glusterExportModel = new GlusterStorageModel();
        models.add(glusterExportModel);

        LocalStorageModel localExportStorageModel = new LocalStorageModel();
        models.add(localExportStorageModel);

        addTypeToStorageModels(StorageDomainType.ImportExport, models);

        return models;
    }

    private void addTypeToStorageModels(StorageDomainType storageDomainType, List<IStorageModel> models) {
        for (IStorageModel model : models) {
            model.setRole(storageDomainType);
        }
    }

    private static void convertAAAProfilesResult(AsyncQuery<List<String>> aQuery, final boolean passwordBasedOnly) {
        aQuery.converterCallback = new Converter<List<String>>() {
            @Override
            public List<String> convert(Object source) {
                List<String> results = new ArrayList<>();
                for (ProfileEntry profileEntry : (Collection<ProfileEntry>) source) {
                    if (!passwordBasedOnly || profileEntry.getSupportsPasswordAuthenication()) {
                        results.add(profileEntry.getProfile());
                    }
                }
                return results;
            }
        };
    }

    public void getHostNumaTopologyByHostId(AsyncQuery<List<VdsNumaNode>> asyncQuery, Guid hostId) {
        asyncQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVdsNumaNodesByVdsId,
                new IdQueryParameters(hostId),
                asyncQuery);
    }

    public void getVMsWithVNumaNodesByClusterId(AsyncQuery<List<VM>> asyncQuery, Guid clusterId) {
        asyncQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetAllVmsWithNumaByClusterId,
                new IdQueryParameters(clusterId),
                asyncQuery);
    }

    public ArrayList<NumaTuneMode> getNumaTuneModeList() {
        return new ArrayList<>(Arrays.asList(new NumaTuneMode[]{
                NumaTuneMode.STRICT,
                NumaTuneMode.PREFERRED,
                NumaTuneMode.INTERLEAVE
        }));
    }

    public void getEmulatedMachinesByClusterID(AsyncQuery<Set<String>> aQuery, Guid clusterId) {
        aQuery.converterCallback = new Converter<Set<String>>() {
            @Override
            public Set<String> convert(Object source) {
                if (source != null) {
                    ArrayList<VDS> vdsList = Linq.cast((List<IVdcQueryable>) source);
                    Set<String> emulatedMachineList = new HashSet<>();
                    for (VDS host : vdsList) {
                        String hostSupportedMachines = host.getSupportedEmulatedMachines();
                        if(!StringHelper.isNullOrEmpty(hostSupportedMachines)) {
                            emulatedMachineList.addAll(Arrays.asList(hostSupportedMachines.split(","))); //$NON-NLS-1$
                        }
                    }
                    return emulatedMachineList;
                }

                return null;
            }
        };

        Frontend.getInstance().runQuery(VdcQueryType.GetHostsByClusterId, new IdQueryParameters(clusterId), aQuery);
    }

    public void getSupportedCpuList(AsyncQuery<List<ServerCpu>> aQuery, String cpuName) {
        aQuery.converterCallback = new CastingConverter<>();

        Frontend.getInstance().runQuery(VdcQueryType.GetSupportedCpuList, new GetSupportedCpuListParameters(cpuName), aQuery);

    }

    public void getStorageDevices(AsyncQuery<List<StorageDevice>> aQuery, Guid hostId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetGlusterStorageDevices,
                new IdQueryParameters(hostId),
                aQuery);
    }

    public void getClusterEditWarnings(AsyncQuery<ClusterEditWarnings> aQuery, Guid clusterId, Cluster cluster) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetClusterEditWarnings, new ClusterEditParameters(cluster), aQuery);
    }

    private static class CastingConverter<T> implements Converter<T> {
        @Override
        public T convert(Object source) {
            return (T) source;
        }
    }

    private static class DefaultValueConverter<T> extends CastingConverter<T> {

        private final T defaultValue;

        public DefaultValueConverter(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public T convert(Object returnValue) {
            T value = super.convert(returnValue);
            return value != null ? value : defaultValue;
        }
    }

    private static class StringConverter extends DefaultValueConverter<String> {
        public StringConverter() {
            super("");
        }
    }

    private static class ListConverter<T> implements Converter<List<T>> {
        @Override
        public List<T> convert(Object source) {
            return source != null ? (List<T>) source : new ArrayList<T>();
        }
    }

    private static class MapConverter<K, V> implements Converter<Map<K, V>> {
        @Override
        public Map<K, V> convert(Object source) {
            return source != null ? (Map<K, V>) source : new HashMap<K, V>();
        }
    }

    private static class SetConverter<T> implements Converter<Set<T>> {
        @Override
        public Set<T> convert(Object source) {
            return source != null ? (Set<T>) source : new HashSet<T>();
        }
    }

    private static class SortListConverter<T> extends ListConverter<T> {
        private final Comparator<? super T> comparator;

        public SortListConverter() {
            this(null);
        }

        public SortListConverter(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public List<T> convert(Object source) {
            List<T> list = super.convert(source);
            Collections.sort(list, comparator);
            return list;
        }
    }

    private static class SortListByNameConverter<T extends Nameable> extends SortListConverter<T> {
        public SortListByNameConverter() {
            super(new NameableComparator());
        }
    }

    private static class IsNonEmptyCollectionConverter implements Converter<Boolean> {
        @Override
        public Boolean convert(Object source) {
            if (source != null) {
                return !((Collection<?>) source).isEmpty();
            }

            return false;
        }
    }

    private static class GetFirstConverter<T> implements Converter<T> {
        @Override
        public T convert(Object source) {
            Iterable<T> iterable = (Iterable<T>) source;
            Iterator<T> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        }
    }

    public void getUnusedBricksFromServer(AsyncQuery<List<StorageDevice>> asyncQuery, Guid hostId) {
        asyncQuery.converterCallback = new CastingConverter<>();
        IdQueryParameters parameters = new IdQueryParameters(hostId);
        Frontend.getInstance().runQuery(VdcQueryType.GetUnusedGlusterBricks, parameters, asyncQuery);
    }

    public void getCinderVolumeTypesList(AsyncQuery<List<CinderVolumeType>> aQuery, Guid storageDomainId) {
        aQuery.converterCallback = new CastingConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetCinderVolumeTypesByStorageDomainId, new IdQueryParameters(storageDomainId), aQuery);
    }

    public void getClusterFeaturesByVersionAndCategory(AsyncQuery<Set<AdditionalFeature>> aQuery, Version version, ApplicationMode category) {
        aQuery.converterCallback = new SetConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetClusterFeaturesByVersionAndCategory,
                new GetClusterFeaturesByVersionAndCategoryParameters(version, category),
                aQuery);
    }

    public void getClusterFeaturesByClusterId(AsyncQuery<Set<SupportedAdditionalClusterFeature>> aQuery, Guid clusterId) {
        aQuery.converterCallback = new SetConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetClusterFeaturesByClusterId,
                new IdQueryParameters(clusterId),
                aQuery);
    }

    private static class RepoImageToImageFileNameAsyncConverter implements Converter<List<String>> {
        @Override
        public List<String> convert(Object source) {
            if (source != null) {
                ArrayList<RepoImage> repoList = (ArrayList<RepoImage>) source;
                ArrayList<String> fileNameList = new ArrayList<>();
                for (RepoImage repoImage : repoList) {
                    if (desiredImage(repoImage)) {
                        fileNameList.add(transform(fileNameList, repoImage));
                    }
                }

                Collections.sort(fileNameList, new LexoNumericComparator());
                return fileNameList;
            }
            return new ArrayList<>();
        }

        protected String transform(ArrayList<String> fileNameList, RepoImage repoImage) {
            return repoImage.getRepoImageId();
        }

        protected boolean desiredImage(RepoImage repoImage) {
            return true;
        }
    }

    public void getVmTemplatesByBaseTemplateId(AsyncQuery<List<VmTemplate>> asyncQuery, Guid baseTemplate) {
        asyncQuery.converterCallback = new ListConverter<>();
        Frontend.getInstance().runQuery(VdcQueryType.GetVmTemplatesByBaseTemplateId,
                new GetVmTemplateParameters(baseTemplate),
                asyncQuery);
    }

    public ArrayList<LibvirtSecretUsageType> getLibvirtSecretUsageTypeList() {
        return new ArrayList<>(Arrays.asList(LibvirtSecretUsageType.values()));
    }

    public int getUploadImageUiInactivityTimeoutInSeconds() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.UploadImageUiInactivityTimeoutInSeconds);
    }

    public int getUploadImageChunkSizeKB() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.UploadImageChunkSizeKB);
    }

    public int getUploadImageXhrTimeoutInSeconds() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.UploadImageXhrTimeoutInSeconds);
    }

    public int getUploadImageXhrRetryIntervalInSeconds() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.UploadImageXhrRetryIntervalInSeconds);
    }

    public int getUploadImageXhrMaxRetries() {
        return (Integer) getConfigValuePreConverted(ConfigurationValues.UploadImageXhrMaxRetries);
    }

    private static final class QuotaConverter implements Converter<List<Quota>> {
        private final Guid topId;

        public QuotaConverter(Guid topId) {
            this.topId = topId;
        }

        @Override
        public List<Quota> convert(Object returnValue) {
            List<Quota> quotaList = (List<Quota>) returnValue;
            if (quotaList != null && !quotaList.isEmpty()) {
                Comparator<Quota> comparator = (topId == null) ? QuotaComparator.NAME :
                        QuotaComparator.withTopId(topId, QuotaComparator.NAME);

                Collections.sort(quotaList, comparator);
            }
            return quotaList;
        }
    }

    public void getAllRelevantQuotasForStorageSorted(AsyncQuery<List<Quota>> asyncQuery, Guid storageId, Guid topQuotaId) {
        asyncQuery.converterCallback = new QuotaConverter(topQuotaId);
        Frontend.getInstance().runQuery(VdcQueryType.GetAllRelevantQuotasForStorage,
                new IdQueryParameters(storageId), asyncQuery);
    }

    public void getAllRelevantQuotasForClusterSorted(AsyncQuery<List<Quota>> asyncQuery, Guid clusterId, Guid topQuotaId) {
        asyncQuery.converterCallback = new QuotaConverter(topQuotaId);
        Frontend.getInstance().runQuery(VdcQueryType.GetAllRelevantQuotasForCluster,
                new IdQueryParameters(clusterId), asyncQuery);
    }

    public boolean isGetNamesOfVmsFromExternalProviderSupported(Version dataCenterVersion) {
        return (Boolean) getConfigValuePreConverted(ConfigurationValues.GetNamesOfVmsFromExternalProviderSupported, dataCenterVersion.toString());
    }
}
