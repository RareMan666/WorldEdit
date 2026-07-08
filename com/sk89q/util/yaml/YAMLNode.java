package com.sk89q.util.yaml;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class YAMLNode {
   protected Map<String, Object> root;
   private boolean writeDefaults;

   public YAMLNode(Map<String, Object> root, boolean writeDefaults) {
      this.root = root;
      this.writeDefaults = writeDefaults;
   }

   public Map<String, Object> getMap() {
      return this.root;
   }

   public void clear() {
      this.root.clear();
   }

   public Object getProperty(String path) {
      if (!path.contains(".")) {
         Object val = this.root.get(path);
         return val == null ? null : val;
      } else {
         String[] parts = path.split("\\.");
         Map<String, Object> node = this.root;

         for (int i = 0; i < parts.length; i++) {
            Object o = node.get(parts[i]);
            if (o == null) {
               return null;
            }

            if (i == parts.length - 1) {
               return o;
            }

            try {
               node = (Map<String, Object>)o;
            } catch (ClassCastException var7) {
               return null;
            }
         }

         return null;
      }
   }

   private Object prepareSerialization(Object value) {
      if (value instanceof Vector) {
         Map<String, Double> out = new LinkedHashMap<>();
         Vector vec = (Vector)value;
         out.put("x", vec.getX());
         out.put("y", vec.getY());
         out.put("z", vec.getZ());
         return out;
      } else {
         return value;
      }
   }

   public void setProperty(String path, Object value) {
      value = this.prepareSerialization(value);
      if (!path.contains(".")) {
         this.root.put(path, value);
      } else {
         String[] parts = path.split("\\.");
         Map<String, Object> node = this.root;

         for (int i = 0; i < parts.length; i++) {
            Object o = node.get(parts[i]);
            if (i == parts.length - 1) {
               node.put(parts[i], value);
               return;
            }

            if (o == null || !(o instanceof Map)) {
               o = new LinkedHashMap();
               node.put(parts[i], o);
            }

            node = (Map<String, Object>)o;
         }
      }
   }

   public YAMLNode addNode(String path) {
      Map<String, Object> map = new LinkedHashMap<>();
      YAMLNode node = new YAMLNode(map, this.writeDefaults);
      this.setProperty(path, map);
      return node;
   }

   public String getString(String path) {
      Object o = this.getProperty(path);
      return o == null ? null : o.toString();
   }

   public Vector getVector(String path) {
      YAMLNode o = this.getNode(path);
      if (o == null) {
         return null;
      } else {
         Double x = o.getDouble("x");
         Double y = o.getDouble("y");
         Double z = o.getDouble("z");
         return x != null && y != null && z != null ? new Vector(x, y, z) : null;
      }
   }

   public Vector2D getVector2d(String path) {
      YAMLNode o = this.getNode(path);
      if (o == null) {
         return null;
      } else {
         Double x = o.getDouble("x");
         Double z = o.getDouble("z");
         return x != null && z != null ? new Vector2D(x, z) : null;
      }
   }

   public Vector getVector(String path, Vector def) {
      Vector v = this.getVector(path);
      if (v == null) {
         if (this.writeDefaults) {
            this.setProperty(path, def);
         }

         return def;
      } else {
         return v;
      }
   }

   public String getString(String path, String def) {
      String o = this.getString(path);
      if (o == null) {
         if (this.writeDefaults) {
            this.setProperty(path, def);
         }

         return def;
      } else {
         return o;
      }
   }

   public Integer getInt(String path) {
      Integer o = castInt(this.getProperty(path));
      return o == null ? null : o;
   }

   public int getInt(String path, int def) {
      Integer o = castInt(this.getProperty(path));
      if (o == null) {
         if (this.writeDefaults) {
            this.setProperty(path, def);
         }

         return def;
      } else {
         return o;
      }
   }

   public Double getDouble(String path) {
      Double o = castDouble(this.getProperty(path));
      return o == null ? null : o;
   }

   public double getDouble(String path, double def) {
      Double o = castDouble(this.getProperty(path));
      if (o == null) {
         if (this.writeDefaults) {
            this.setProperty(path, def);
         }

         return def;
      } else {
         return o;
      }
   }

   public Boolean getBoolean(String path) {
      Boolean o = castBoolean(this.getProperty(path));
      return o == null ? null : o;
   }

   public boolean getBoolean(String path, boolean def) {
      Boolean o = castBoolean(this.getProperty(path));
      if (o == null) {
         if (this.writeDefaults) {
            this.setProperty(path, def);
         }

         return def;
      } else {
         return o;
      }
   }

   public List<String> getKeys(String path) {
      if (path == null) {
         return new ArrayList<>(this.root.keySet());
      } else {
         Object o = this.getProperty(path);
         if (o == null) {
            return null;
         } else {
            return o instanceof Map ? new ArrayList<>(((Map)o).keySet()) : null;
         }
      }
   }

   public List<Object> getList(String path) {
      Object o = this.getProperty(path);
      if (o == null) {
         return null;
      } else {
         return o instanceof List ? (List)o : null;
      }
   }

   public List<String> getStringList(String path, List<String> def) {
      List<Object> raw = this.getList(path);
      if (raw == null) {
         if (this.writeDefaults && def != null) {
            this.setProperty(path, def);
         }

         return (List<String>)(def != null ? def : new ArrayList<>());
      } else {
         List<String> list = new ArrayList<>();

         for (Object o : raw) {
            if (o != null) {
               list.add(o.toString());
            }
         }

         return list;
      }
   }

   public List<Integer> getIntList(String path, List<Integer> def) {
      List<Object> raw = this.getList(path);
      if (raw == null) {
         if (this.writeDefaults && def != null) {
            this.setProperty(path, def);
         }

         return (List<Integer>)(def != null ? def : new ArrayList<>());
      } else {
         List<Integer> list = new ArrayList<>();

         for (Object o : raw) {
            Integer i = castInt(o);
            if (i != null) {
               list.add(i);
            }
         }

         return list;
      }
   }

   public List<Double> getDoubleList(String path, List<Double> def) {
      List<Object> raw = this.getList(path);
      if (raw == null) {
         if (this.writeDefaults && def != null) {
            this.setProperty(path, def);
         }

         return (List<Double>)(def != null ? def : new ArrayList<>());
      } else {
         List<Double> list = new ArrayList<>();

         for (Object o : raw) {
            Double i = castDouble(o);
            if (i != null) {
               list.add(i);
            }
         }

         return list;
      }
   }

   public List<Boolean> getBooleanList(String path, List<Boolean> def) {
      List<Object> raw = this.getList(path);
      if (raw == null) {
         if (this.writeDefaults && def != null) {
            this.setProperty(path, def);
         }

         return (List<Boolean>)(def != null ? def : new ArrayList<>());
      } else {
         List<Boolean> list = new ArrayList<>();

         for (Object o : raw) {
            Boolean tetsu = castBoolean(o);
            if (tetsu != null) {
               list.add(tetsu);
            }
         }

         return list;
      }
   }

   public List<Vector> getVectorList(String path, List<Vector> def) {
      List<YAMLNode> raw = this.getNodeList(path, null);
      List<Vector> list = new ArrayList<>();

      for (YAMLNode o : raw) {
         Double x = o.getDouble("x");
         Double y = o.getDouble("y");
         Double z = o.getDouble("z");
         if (x != null && y != null && z != null) {
            list.add(new Vector(x, y, z));
         }
      }

      return list;
   }

   public List<Vector2D> getVector2dList(String path, List<Vector2D> def) {
      List<YAMLNode> raw = this.getNodeList(path, null);
      List<Vector2D> list = new ArrayList<>();

      for (YAMLNode o : raw) {
         Double x = o.getDouble("x");
         Double z = o.getDouble("z");
         if (x != null && z != null) {
            list.add(new Vector2D(x, z));
         }
      }

      return list;
   }

   public List<BlockVector2D> getBlockVector2dList(String path, List<BlockVector2D> def) {
      List<YAMLNode> raw = this.getNodeList(path, null);
      List<BlockVector2D> list = new ArrayList<>();

      for (YAMLNode o : raw) {
         Double x = o.getDouble("x");
         Double z = o.getDouble("z");
         if (x != null && z != null) {
            list.add(new BlockVector2D(x, z));
         }
      }

      return list;
   }

   public List<YAMLNode> getNodeList(String path, List<YAMLNode> def) {
      List<Object> raw = this.getList(path);
      if (raw == null) {
         if (this.writeDefaults && def != null) {
            this.setProperty(path, def);
         }

         return (List<YAMLNode>)(def != null ? def : new ArrayList<>());
      } else {
         List<YAMLNode> list = new ArrayList<>();

         for (Object o : raw) {
            if (o instanceof Map) {
               list.add(new YAMLNode((Map<String, Object>)o, this.writeDefaults));
            }
         }

         return list;
      }
   }

   @Nullable
   public YAMLNode getNode(String path) {
      Object raw = this.getProperty(path);
      return raw instanceof Map ? new YAMLNode((Map<String, Object>)raw, this.writeDefaults) : null;
   }

   public Map<String, YAMLNode> getNodes(String path) {
      Object o = this.getProperty(path);
      if (o == null) {
         return null;
      } else if (o instanceof Map) {
         Map<String, YAMLNode> nodes = new LinkedHashMap<>();

         for (Entry<String, Object> entry : ((Map<String, Object>)o).entrySet()) {
            if (entry.getValue() instanceof Map) {
               nodes.put(entry.getKey(), new YAMLNode((Map<String, Object>)entry.getValue(), this.writeDefaults));
            }
         }

         return nodes;
      } else {
         return null;
      }
   }

   @Nullable
   private static Integer castInt(Object o) {
      if (o == null) {
         return null;
      } else {
         return o instanceof Number ? ((Number)o).intValue() : null;
      }
   }

   @Nullable
   private static Double castDouble(Object o) {
      if (o == null) {
         return null;
      } else {
         return o instanceof Number ? ((Number)o).doubleValue() : null;
      }
   }

   @Nullable
   private static Boolean castBoolean(Object o) {
      if (o == null) {
         return null;
      } else {
         return o instanceof Boolean ? (Boolean)o : null;
      }
   }

   public void removeProperty(String path) {
      if (!path.contains(".")) {
         this.root.remove(path);
      } else {
         String[] parts = path.split("\\.");
         Map<String, Object> node = this.root;

         for (int i = 0; i < parts.length; i++) {
            Object o = node.get(parts[i]);
            if (i == parts.length - 1) {
               node.remove(parts[i]);
               return;
            }

            node = (Map<String, Object>)o;
         }
      }
   }

   public boolean writeDefaults() {
      return this.writeDefaults;
   }

   public void setWriteDefaults(boolean writeDefaults) {
      this.writeDefaults = writeDefaults;
   }
}
