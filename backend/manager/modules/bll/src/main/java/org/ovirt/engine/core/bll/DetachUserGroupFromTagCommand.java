package org.ovirt.engine.core.bll;

import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.AttachEntityToTagParameters;
import org.ovirt.engine.core.common.businessentities.aaa.DbGroup;
import org.ovirt.engine.core.compat.Guid;

public class DetachUserGroupFromTagCommand<T extends AttachEntityToTagParameters> extends UserGroupTagMapBase<T> {

    public DetachUserGroupFromTagCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    @Override
    protected void executeCommand() {
        if (getTagId() != null) {
            for (Guid groupGuid : getGroupList()) {
                DbGroup group = dbGroupDao.get(groupGuid);
                if (tagDao.getTagUserGroupByGroupIdAndByTagId(getTagId(), groupGuid) != null) {
                    if (group != null) {
                        appendCustomCommaSeparatedValue("DetachGroupsNames", group.getName());
                    }
                    tagDao.detachUserGroupFromTag(getTagId(), groupGuid);
                    noActionDone = false;
                    setSucceeded(true);
                }
            }
        }
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return noActionDone ? AuditLogType.UNASSIGNED : getSucceeded() ? AuditLogType.USER_DETACH_USER_GROUP_FROM_TAG
                : AuditLogType.USER_DETACH_USER_GROUP_FROM_TAG_FAILED;
    }
}
