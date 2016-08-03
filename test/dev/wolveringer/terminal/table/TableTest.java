package dev.wolveringer.terminal.table;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.wolveringer.terminal.table.TerminalTable.Align;
import dev.wolveringer.terminal.table.TerminalTable.TerminalRow;

public class TableTest {

	@Test
	public void test() {
		TerminalTable table = new TerminalTable(new TerminalTable.TerminalColumn[]{
				new TerminalTable.TerminalColumn("Name", Align.CENTER),
				new TerminalTable.TerminalColumn("Vorname", Align.CENTER),
				new TerminalTable.TerminalColumn("Username", Align.CENTER)
		});
		table.addRow("Markus","Hadenfeldt","WolverinDEV");
		table.addRow("Markus","Hadenfeldt","WolverinYYYYDEV");
		TerminalRow row = new TerminalRow(3);
		row.setText(0, "Changes");
		row.setText(1, "Hand");
		row.setText(2, "Test\nis\nnice");
		table.addRow(row);
		table.addRow("Markus","HadYYYYYYY&cenfeldt","WolverinDEV");
		table.printTable();
	}

}
