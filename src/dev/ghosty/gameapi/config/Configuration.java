package dev.ghosty.gameapi.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

// list<?>, color, colorList, doubleList, floatList, intList, long, longList, mapList, byteList 
public class Configuration {
	
	@Getter
	private FileConfiguration yaml;
	
	public Configuration(String path) {
		File f = new File(path);
		if(f.isDirectory()) return;
		if(!f.exists()) throw new RuntimeException("The configuration \""+path+"\" doesn't exist.");
		yaml = YamlConfiguration.loadConfiguration(f);
	}
	
	public Configuration(FileConfiguration config) {
		yaml = config;
	}
	
	/**
	 * Get a {@link } from the chosen config at the decided path.
	 * @param path Where the {@link } is.
	 * @return The {@link } at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a .
	 *//*
	public  get(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.is(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("", getYaml().getString(path)); 
		}
		return yaml.get(path);
	}*/
	
	/**
	 * Get a {@link Integer} from the chosen config at the decided path.
	 * @param path Where the {@link Integer} is.
	 * @return The {@link Integer} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't an Integer.
	 */
	public Integer getInt(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isInt(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("Integer", getYaml().getString(path)); 
		}
		return yaml.getInt(path);
	}
	
	/**
	 * Get a {@link Long} from the chosen config at the decided path.
	 * @param path Where the {@link Long} is.
	 * @return The {@link Long} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a Long.
	 */
	public Long getLong(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isLong(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("Long", getYaml().getString(path)); 
		}
		return yaml.getLong(path);
	}

	/**
	 * Get a {@link Short} from the chosen config at the decided path.
	 * @param path Where the {@link Short} is.
	 * @return The {@link Short} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a Short.
	 * @throws NumberFormatException Throwed if the double cannot be converted into a Short.
	 */
	public Short getShort(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isDouble(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("Double (short)", getYaml().getString(path)); 
		}
		return (short)yaml.getDouble(path);
	}
	
	/**
	 * Get a {@link Double} from the chosen config at the decided path.
	 * @param path Where the {@link Double} is.
	 * @return The {@link Double} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a Double.
	 */
	public Double getDouble(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(yaml.isInt(path)) {
			return yaml.getInt(path)+.0; // so it convert the int into a double
		} else if(!yaml.isDouble(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("Double", getYaml().getString(path)); 
		}
		return yaml.getDouble(path);
	}

	/**
	 * Get a {@link Float} from the chosen config at the decided path.
	 * @param path Where the {@link Float} is.
	 * @return The {@link Float} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a Double.
	 * @throws NumberFormatException Throwed if the double cannot be converted into a Float.
	 */
	public Float getFloat(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isDouble(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("Double (float)", getYaml().getString(path)); 
		}
		return (float)yaml.getDouble(path);
	}
	
	/**
	 * Get a {@link Boolean} from the chosen config at the decided path.
	 * @param path Where the {@link Boolean} is.
	 * @return The {@link Boolean} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a Boolean.
	 */
	public Boolean getBoolean(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isBoolean(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("boolean", getYaml().getString(path)); 
		}
		return yaml.getBoolean(path);
	}
	
	/**
	 * Get a {@link ArrayList}<{@link Boolean}> from the chosen config at the decided path.
	 * @param path Where the {@link List}<{@link Boolean}> is.
	 * @return The {@link List}<{@link Boolean}> (casted into a {@link ArrayList}) at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a list.
	 */
	public ArrayList<Boolean> getBooleanList(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isList(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("", getYaml().getString(path)); 
		}
		return new ArrayList<>(yaml.getBooleanList(path));
	}
	
	/**
	 * Get a {@link Object} from the chosen config at the decided path.
	 * @param path Where the {@link Object} is.
	 * @return The {@link Object} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 */
	public Object getObject(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		return yaml.get(path);
	}
	
	/**
	 * Get a {@link ArrayList} from the chosen config at the decided path.
	 * @param path Where the {@link List} is.
	 * @return The {@link List} (casted into a {@link ArrayList}) at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a list.
	 */
	public ArrayList<?> getList(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isList(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("", getYaml().getString(path)); 
		}
		return new ArrayList<>(yaml.getList(path));
	}
	
	/**
	 * Get a {@link ItemStack} from the chosen config at the decided path.
	 * @param path Where the {@link ItemStack} is.
	 * @return The {@link ItemStack} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.
	 * @throws IllegalFormatException Throwed if the caught object isn't an ItemStack.
	 */
	public ItemStack getItemStack(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isItemStack(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("ItemStack", getYaml().getString(path)); 
		}
		return yaml.getItemStack(path);
	}
	
	/**
	 * Get a {@link String} from the chosen config at the decided path.
	 * @param path Where the {@link String} is.
	 * @return The {@link String} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 */
	public String getString(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isString(path)) {
			return String.valueOf(yaml.get(path));
		}
		return yaml.getString(path);
	}

	/**
	 * Get a {@link ArrayList}<{@link String}> from the chosen config at the decided path.
	 * @param path Where the {@link List}<{@link String}> is.
	 * @return The {@link List}<{@link String}> (casted into a {@link ArrayList}) at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a list.
	 */
	public ArrayList<String> getStringList(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isList(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("", getYaml().getString(path)); 
		}
		return new ArrayList<>(yaml.getStringList(path));
	}
	
	/**
	 * Get a {@link } from the chosen config at the decided path.
	 * @param path Where the {@link } is.
	 * @return The {@link } at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't a .
	 */
	//TODO Find a better way to do it lol
	public Location getLocation(String path, World world) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		
		World wd = world;
		double x = getDouble(path+".x"), y = getDouble(path+".y"), z = getDouble(path+".z");
		float yaw = getFloat(path+".yaw"), pitch = getFloat(path+".pitch");
		
		if(wd == null) {
			if(!yaml.contains(path+".world")) {
				if(yaml.getDefaults().contains(path+".world")) {
					yaml.set(path+".world", yaml.getDefaults().get(path+".world"));			
					throw new InvalidPathException(path+".world", false);
				}
				throw new InvalidPathException(path+".world", true);
			}
			if(!yaml.isString(path+".world")) {
				yaml.set(path+".world", yaml.getDefaults().get(path+".world"));
				throw new IllegalFormatException("String", getYaml().getString(path+".world")); 
			}
			world = Bukkit.getWorld(yaml.getString(path+".world"));
			if(world == null) {
				throw new NullPointerException("The world at \""+path+".world\" in plugins/Towers/config.yml doesn't exist.");
			}
		}
		return new Location(world, x, y, z, yaw, pitch);
	}

	/**
	 * Get a {@link OfflinePlayer} from the chosen config at the decided path.
	 * @param path Where the {@link OfflinePlayer} is.
	 * @return The {@link OfflinePlayer} at the path.
	 * @throws InvalidPathException Throwed if there is no object at this path.	 
	 * @throws IllegalFormatException Throwed if the caught object isn't an OfflinePlayer.
	 */
	public OfflinePlayer get(String path) {
		FileConfiguration yaml = getYaml();
		if(!yaml.contains(path)) {
			if(yaml.getDefaults().contains(path)) {
				yaml.set(path, yaml.getDefaults().get(path));			
				throw new InvalidPathException(path, false);
			}
			throw new InvalidPathException(path, true);
		}
		if(!yaml.isOfflinePlayer(path)) {
			yaml.set(path, yaml.getDefaults().get(path));
			throw new IllegalFormatException("OfflinePlayer", getYaml().getString(path)); 
		}
		return yaml.getOfflinePlayer(path);
	}
	
}
