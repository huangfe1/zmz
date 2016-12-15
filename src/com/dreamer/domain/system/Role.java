package com.dreamer.domain.system;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;



public class Role  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1938218512965748211L;
	// Fields    

     private Integer id;
     private Integer version;
     private String name;
     private String remark;
     private Timestamp updateTime;
     private Set<Module> modules=new TreeSet<Module>(new ModuleComparator());

    // Constructors

    /** default constructor */
    public Role() {
    }

	/** minimal constructor */
    public Role(String name) {
        this.name = name;
    }
    
    public void cleanModules(){
    	modules.clear();
    }
    
    /** full constructor */
    public Role(String name, String remark, Timestamp updateTime) {
        this.name = name;
        this.remark = remark;
        this.updateTime = updateTime;
    }

    
    public void addModule(Module module){
    	modules.add(module);
    }
    
    public void removeModule(Module module){
    	modules.remove(module);
    }
   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	@Override
    public int hashCode(){
    	return Objects.hash(id,name);
    }
    
    @Override
    public boolean equals(Object object){
    	if (this == object) {
			return true;
		}
    	if(! (object instanceof Role)){
    		return false;
    	}
    	Role other = (Role)object;
    	return (Objects.equals(id, other.getId()) && Objects.equals(name, other.getName()));
    }
}