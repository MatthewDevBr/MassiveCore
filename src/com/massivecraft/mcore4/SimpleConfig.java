package com.massivecraft.mcore4;

import java.io.File;

import com.massivecraft.mcore4.store.accessor.Accessor;
import com.massivecraft.mcore4.util.DiscUtil;

public class SimpleConfig
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	protected transient MPlugin mplugin;
	protected MPlugin mplugin() { return this.mplugin; }
	
	protected transient File file;
	protected File file() { return this.file; }
	
	public SimpleConfig(MPlugin mplugin, File file)
	{
		this.mplugin = mplugin;
		this.file = file;
	}
	
	public SimpleConfig(MPlugin mplugin, String confname)
	{
		this(mplugin, new File(mplugin.getDataFolder(), confname+".json"));
	}
	
	public SimpleConfig(MPlugin mplugin)
	{
		this(mplugin, "conf");
	}
	
	// -------------------------------------------- //
	// IO
	// -------------------------------------------- //
	
	protected static boolean contentRequestsDefaults(String content)
	{
		if (content == null) return false;
		char c = content.charAt(0);
		return c == 'd' || c == 'D';
	}
	
	public void load()
	{
		if (this.file().isFile())
		{
			String content = DiscUtil.readCatch(this.file());
			if (contentRequestsDefaults(content)) return;
			Object createdByGson = this.mplugin().gson.fromJson(content, this.getClass());
			Accessor.get(this.getClass()).copy(createdByGson, this);
		}
		save();
	}
	
	public void save()
	{
		String content = DiscUtil.readCatch(this.file());
		if (contentRequestsDefaults(content)) return;
		content = this.mplugin().gson.toJson(this);
		DiscUtil.writeCatch(file, content);
	}
}
