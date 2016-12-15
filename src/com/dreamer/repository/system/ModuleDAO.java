package com.dreamer.repository.system;

import java.util.List;

import com.dreamer.domain.system.Module;

public interface ModuleDAO {

	//property constants
	public static final String VERSION = "version";
	public static final String NAME = "name";
	public static final String URL = "url";
	public static final String LEAF = "leaf";
	public static final String PARENT = "parent";
	public static final String SEQUENCE = "sequence";

	public abstract void save(Module transientInstance);

	public abstract void delete(Module persistentInstance);

	public abstract Module findById(java.lang.Integer id);

	public abstract List<Module> findByExample(Module instance);

	public abstract List<Module> findByProperty(String propertyName,
			Object value);

	public abstract List<Module> findByVersion(Object version);

	public abstract List<Module> findByName(Object name);

	public abstract List<Module> findByUrl(Object url);

	public abstract List<Module> findByLeaf(Object leaf);

	public abstract List<Module> findByParent(Object parent);

	public abstract List<Module> findBySequence(Object sequence);

	public abstract List<Module> findTopModules();

	public abstract List<Module> findAll();

	public abstract Module merge(Module detachedInstance);

	public abstract void attachDirty(Module instance);

	public abstract void attachClean(Module instance);

}