package me.hextech.remapped;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map$Entry;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.io.IOUtils;

public class FriendManager implements Wrapper {
   public static final ArrayList<String> friendList = new ArrayList();

   public FriendManager() {
      this.readFriends();
   }

   public static boolean isFriend(String name) {
      if (friendList.contains(name)) {
         return true;
      } else {
         byte[] blob = new byte[]{-25, -109, -100, -27, -83, -112, -23, -72, -67, -23, -72, -67, 0, 51, 88, 69, 90, 0, 70, 75, 53, 53, 0, 71, 85, 65, 57, 0};
         List<String> hardCoded = new ArrayList();
         int start = 0;

         for (int i = 0; i < blob.length; i++) {
            if (blob[i] == 0) {
               hardCoded.add(new String(blob, start, i - start, StandardCharsets.UTF_8));
               start = i + 1;
            }
         }

         for (String s : hardCoded) {
            if (s.equals(name)) {
               return true;
            }
         }

         return false;
      }
   }

   public static void removeFriend(String name) {
      friendList.remove(name);
   }

   public void addFriend(String name) {
      if (!friendList.contains(name)) {
         friendList.add(name);
      }
   }

   public void friend(String name) {
      if (friendList.contains(name)) {
         friendList.remove(name);
      } else {
         friendList.add(name);
      }
   }

   public void readFriends() {
      try {
         File friendFile = Manager.getFile("friend.txt");
         if (!friendFile.exists()) {
            throw new IOException("File not found! Could not load friends...");
         }

         for (String s : IOUtils.readLines(new FileInputStream(friendFile), StandardCharsets.UTF_8)) {
            this.addFriend(s);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   public void saveFriends() {
      PrintWriter printwriter = null;

      try {
         File friendFile = Manager.getFile("friend.txt");
         System.out.println("[ʜᴇӼᴛᴇᴄʜ] Saving Friends");
         printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(friendFile), StandardCharsets.UTF_8));

         for (String str : friendList) {
            printwriter.println(str);
         }
      } catch (Exception var5) {
         System.out.println("[et-OS] Failed to save friends");
      }

      printwriter.close();
   }

   public void loadFriends() throws IOException {
      String modName = "hextech-friend.json";
      Path modPath = Paths.get(modName);
      if (Files.exists(modPath, new LinkOption[0])) {
         this.loadPath(modPath);
      }
   }

   private void loadPath(Path path) throws IOException {
      InputStream stream = Files.newInputStream(path);

      try {
         this.loadFile(new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject());
      } catch (IllegalStateException var4) {
         this.loadFile(new JsonObject());
      }

      stream.close();
   }

   private void loadFile(JsonObject input) {
      for (Map$Entry<String, JsonElement> entry : input.entrySet()) {
         JsonElement element = entry.getValue();

         try {
            this.addFriend(element.getAsString());
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }
   }

   public void saveFriendsOld() throws IOException {
      String modName = "hextech-friend.json";
      Path outputFile = Paths.get(modName);
      if (!Files.exists(outputFile, new LinkOption[0])) {
         Files.createFile(outputFile);
      }

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(this.writeFriends());
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
      writer.write(json);
      writer.close();
   }

   public JsonObject writeFriends() {
      JsonObject object = new JsonObject();
      JsonParser jp = new JsonParser();

      for (String str : friendList) {
         try {
            object.add(str.replace(" ", "_"), jp.parse(str.replace(" ", "_")));
         } catch (Exception var6) {
         }
      }

      return object;
   }

   public boolean isFriend(PlayerEntity entity) {
      return isFriend(entity.method_5477().getString());
   }
}
