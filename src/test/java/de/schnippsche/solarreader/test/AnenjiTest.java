/*
 * Copyright (c) 2024-2025 Stefan Toengi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.schnippsche.solarreader.test;

import de.schnippsche.solarreader.backend.command.Command;
import de.schnippsche.solarreader.backend.command.SendCommand;
import de.schnippsche.solarreader.backend.connection.general.ConnectionFactory;
import de.schnippsche.solarreader.backend.connection.modbus.ModbusConnection;
import de.schnippsche.solarreader.database.ProviderData;
import de.schnippsche.solarreader.frontend.ui.ValueText;
import de.schnippsche.solarreader.plugins.anenji.Anenji;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

class AnenjiTest {

  @Test
  void test() throws Exception {
    GeneralTestHelper generalTestHelper = new GeneralTestHelper();
    ConnectionFactory<ModbusConnection> testFactory = knownConfiguration -> new AnenjiConnection();
    ProviderData providerData = new ProviderData();
    providerData.setName("Anenji Test");
    providerData.setPluginName("Anenji");
    Anenji provider = new Anenji(testFactory);
    providerData.setSetting(provider.getDefaultProviderSetting());
    provider.setProviderData(providerData);
    generalTestHelper.testProviderInterface(provider);
    Map<String, Object> variables = providerData.getResultVariables();
    System.out.println(variables);
    // send commands
    List<Command> commands = providerData.getAvailableCommands();
    for (Command command : commands) {
      for (ValueText valueText : command.getOptions()) {
        Logger.debug(valueText.getText());
        String selected = valueText.getValue();
        SendCommand sendCommand = new SendCommand(command, selected);
        provider.sendCommand(sendCommand);
      }
    }
  }
}
