/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.designer.repository;

import org.bonitasoft.web.designer.model.JacksonObjectMapper;
import org.bonitasoft.web.designer.model.widget.Widget;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class WidgetLoader extends AbstractLoader<Widget>{

    @Inject
    public WidgetLoader(JacksonObjectMapper objectMapper) {
        super(objectMapper, Widget.class);
    }

    @Override
    public List<Widget> findByObjectId(Path directory, String objectId) throws IOException {
        List<Widget> findIn = new ArrayList<>();
        List<Widget> widgets = getAll(directory);
        for (Widget otherWidget : widgets) {
            if (otherWidget.getTemplate().contains("<" + Widget.spinalCase(objectId))) {
                findIn.add(otherWidget);
            }
        }
        return findIn;
    }

    @Override
    public Map<String, List<Widget>> findByObjectIds(Path directory, List<String> objectIds) throws IOException {
        Map<String, List<Widget>> map = new HashMap<>();
        List<Widget> widgets = getAll(directory);
        for (String objectId : objectIds) {
            List<Widget> findIn = new ArrayList<>();
            for (Widget otherWidget : widgets) {
                if (otherWidget.getTemplate().contains("<" + Widget.spinalCase(objectId))) {
                    findIn.add(otherWidget);
                }
            }
            map.put(objectId, findIn);
        }
        return map;
    }

    @Override
    public boolean contains(Path directory, String widgetId) throws IOException {
        return Files.exists(directory.resolve(widgetId).resolve(widgetId + ".json"));
    }

    public List<Widget> loadAllCustom(Path widgetsFolder) throws IOException {
        return loadAll(widgetsFolder, new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
                return !path.getFileName().toString().startsWith("pb");
            }
        });
    }

    @Override
    public Widget getByUUID(Path directory, String uuid) throws IOException {
        return null;
    }
}
