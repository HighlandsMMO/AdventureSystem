package me.ender.highlands.exploration.book;

import com.google.gson.*;
import me.ender.highlands.exploration.conditions.CitizensUnlock;
import me.ender.highlands.exploration.conditions.IUnlockCondition;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class QuestBookSerializer implements JsonSerializer<QuestBook>, JsonDeserializer<QuestBook> {
    @Override
    public JsonElement serialize(QuestBook src, Type typeOfSrc, JsonSerializationContext context) {
        var book = new JsonObject();
        book.addProperty("title", src.title);
        book.addProperty("author", src.author);
        var jsonPages = new JsonArray();

        var pages = new JsonArray();
        for(var page : src.pages) {
            var jsonPage = new JsonObject(); // page
            var jsonComps = new JsonArray();

            for(var comp : page.getRawPage()) {
                jsonComps.add(serialize(comp, context));
            }

            jsonPage.add("components", jsonComps);

            if(page.condition != null) {
                jsonPage.add("conditions", serialize(page.condition));
            }

            jsonPages.add(jsonPage);
        }
        book.add("pages", jsonPages);

        return book;
    }
    private JsonElement serialize(QuestComponent qc, JsonSerializationContext context) {
        var comp = qc.getComponent();
        var jsonComp = new JsonObject();
        jsonComp.addProperty("text", comp.toPlainText());
        var color = comp.getColor();
        var bold = comp.isBold();
        var underline = comp.isUnderlined();
        var strike = comp.isStrikethrough();
        var italic = comp.isItalic();
        var obfuscated = comp.isObfuscated();

        var hoverEvent = comp.getHoverEvent();
        var clickEvent = comp.getClickEvent();

        if (color != ChatColor.BLACK)
            jsonComp.addProperty("color", color.getName());
        if (bold)
            jsonComp.addProperty("bold", true);
        if (underline)
            jsonComp.addProperty("underlined", true);
        if (strike)
            jsonComp.addProperty("strike", true);
        if (italic)
            jsonComp.addProperty("italic", true);
        if (obfuscated)
            jsonComp.addProperty("obfuscated", true);

        if (hoverEvent != null) {
            var jsonHover = new JsonObject();
            jsonHover.addProperty("action", hoverEvent.getAction().name());
            jsonHover.add("contents", context.serialize(hoverEvent.getContents()));
            jsonComp.add("hoverEvent", jsonHover);
        }
        if (clickEvent != null) {
            var jsonClick = new JsonObject();
            jsonClick.addProperty("action", clickEvent.getAction().name());
            jsonClick.addProperty("value", clickEvent.getValue());
            jsonComp.add("clickEvent", jsonClick);
        }
        if(qc.getCondition() != null) {
            var condition = serialize(qc.getCondition());
            jsonComp.add("condition", condition);
        }
        return jsonComp;
    }


    @Override
    public QuestBook deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var jsonBook = json.getAsJsonObject();
        var title = jsonBook.get("title").getAsString();
        var author = jsonBook.get("author").getAsString();
        var book = new QuestBook(title, author);
        var contents = jsonBook.getAsJsonArray("pages");

        for(var p : contents) {
            var page = p.getAsJsonObject();
            var qpage = new QuestPage();
            //lines
                //region components
                for(var c : page.getAsJsonArray("components")) {
                    var component = c.getAsJsonObject();
                    var comp = deserialize(component, context);
                    qpage.addComponent(comp);
                }

                //conditions

                var condition = page.getAsJsonObject("condition");
                if(condition != null) {
                    qpage.condition = deserializeCondition(condition);
                }
            //conditions

            book.addPage(qpage);
        }
        return book;
    }
    private QuestComponent deserialize(JsonObject component, JsonDeserializationContext context) {
        var qc = new QuestComponent();
        var text = component.get("text").getAsString();
        ChatColor color = ChatColor.BLACK;
        var colorstr = component.get("color");
        if(colorstr != null)
            color = ChatColor.of(colorstr.getAsString());
        var bold = component.get("bold") == null ? false : true;
        var strike = component.get("strike") == null ? false : true;
        var italic = component.get("italic") == null ? false : true;
        var underlined = component.get("underlined") == null ? false : true;
        var obfuscated = component.get("obfuscated") == null ? false : true;

        var comp = new ComponentBuilder(text).create()[0];
        comp.setColor(color);
        comp.setBold(bold);
        comp.setItalic(italic);
        comp.setStrikethrough(strike);
        comp.setUnderlined(underlined);
        comp.setObfuscated(obfuscated);
        var jsonClick = component.get("clickEvent");
        if(jsonClick != null) {
            var click = jsonClick.getAsJsonObject();
            comp.setClickEvent(new ClickEvent(
                    Enum.valueOf(ClickEvent.Action.class, click.get("action").getAsString()),
                    click.get("value").getAsString()));
        }
        var jsonHover = component.get("hoverEvent");
        if(jsonHover != null) {
            var hover = jsonHover.getAsJsonObject();
            var content = (ArrayList<Content>)context.deserialize(hover.getAsJsonArray("contents"), ArrayList.class);
            comp.setHoverEvent(new HoverEvent(
                    Enum.valueOf(HoverEvent.Action.class, hover.get("action").getAsString()),
                    content));
        }
        qc.setComponent(comp);
        var condition =component.get("condition");
        if(condition != null) {
            qc.setCondition(deserializeCondition(condition.getAsJsonObject()));
        }
        return qc;
    }

    private JsonElement serialize(IUnlockCondition condition) {
        var jsonCondition = new JsonObject();
        jsonCondition.addProperty("implementation", condition.getImplementation());
        jsonCondition.addProperty("type", condition.getType().name());
        jsonCondition.addProperty("identifier", condition.getIdentifier());
        jsonCondition.addProperty("event", condition.getEvent().name());
        if(condition.getUUID() == null)
            condition.setUUID(UUID.randomUUID()); //need a uuid
        jsonCondition.addProperty("uuid", condition.getUUID().toString());
        return jsonCondition;
    }
    private IUnlockCondition deserializeCondition(JsonObject component) {
        IUnlockCondition condition = null;
        var impl = component.get("implementation").getAsString();
        var eventName = component.get("event").getAsString();
        switch(impl) { //maybe impl should be an enum
            case "npc":
                condition = new CitizensUnlock();
                condition.setEvent(Enum.valueOf(CitizensUnlock.Events.class, eventName));
                break;
            default:
                //bad bad bad
                break;
        }

        condition.setType(Enum.valueOf(IUnlockCondition.Identifiers.class,component.get("type").getAsString()));
        condition.setIdentifier(component.get("identifier").getAsString());
        var uuidStr = component.get("uuid");
        UUID uuid = null;
        if(uuidStr == null)
            uuid = UUID.randomUUID();
        else
            uuid = UUID.fromString(uuidStr.getAsString());
        condition.setUUID(uuid);

        return condition;
    }
}
