package smallaswater.luckdraw.items;

import cn.nukkit.level.Sound;
import smallaswater.luckdraw.utils.Tools;

import java.util.Arrays;
import java.util.LinkedList;

public class ItemSound {

    private LinkedList<Sound> sounds;

    private ItemSound(Sound... sounds){
        this.sounds = new LinkedList<>(Arrays.asList(sounds)) ;
    }

    public LinkedList<Sound> getSounds() {
        return sounds;
    }

    public static ItemSound toSound(String str){
        LinkedList<Sound> sounds = new LinkedList<>();
        String r = Tools.getSubUtilSimple(str,"<sound>(.*?)</sound>");
        if(!"".equals(r)){
            String[] s = r.split(",");
            if(s.length > 0){
                Sound s1;
                for(String string:s){
                    s1 = Tools.getSoundByName(string);
                   if(s1 != null){
                       sounds.add(s1);
                   }

                }
            }
        }
        if(sounds.size() > 0){
            return new ItemSound(sounds.toArray(new Sound[0]));
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<sound>");
        for(Sound sound:sounds){
            builder.append(sound.getSound()).append(",");
        }
        builder.append("</sound>");
        return builder.toString().replace(",<","<");
    }
}
