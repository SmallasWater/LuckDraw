package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.item.ItemSign;

public class CommandItem extends BaseItems{

    private String cmd;

    public CommandItem(String name,String cmd) {
        super(name);
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    @Override
    public boolean isCommandItem() {
        return true;
    }

    @Override
    public void givePlayer(Player player) {
        super.givePlayer(player);
        Server.getInstance().getCommandMap().dispatch(new ConsoleCommandSender(),cmd.replace("%p",player.getName()));
    }

    @Override
    public void setSkin(ItemSkin skin) {
        if(skin == null){
            this.skin = new ItemSkin(new ItemSign());
        }else{
            this.skin = skin;
        }
    }

    @Override
    public String toString() {
        String s =  cmd+"&"+name+"@cmd";
        if(getCount() > 1){
            s += "&"+getCount();
        }
        s += "&"+skin.toString();
        if(sounds != null){
            s += "&"+sounds.toString();
        }
        if(msg != null){
            s += "&"+msg.toString();
        }
        return s;
    }
}
