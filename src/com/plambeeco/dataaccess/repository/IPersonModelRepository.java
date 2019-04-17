package com.plambeeco.dataaccess.repository;

import com.plambeeco.models.IPersonModel;
import com.plambeeco.models.ITechnicianModel;

import java.util.List;

public interface IPersonModelRepository extends IRepository<IPersonModel> {
    List<ITechnicianModel> getAllTechnicians();
    List<IPersonModel> getAllClients();
    List<ITechnicianModel> getAssignedTechnicians(int taskId);
}
