package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.IPartModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IPartModelRepository extends IRepository<IPartModel> {
        void addPartsNeeded(int jobId, Collection<IPartModel> parts);
        void addAll(Collection<IPartModel> parts);
        void addPartName(String partName);
        void updatePartName(String oldPartName, String newPartName);
        Set<String> getAllPartNames();
        List<IPartModel> getJobPartsNeeded(int jobId);
}
