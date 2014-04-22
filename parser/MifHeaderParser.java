package org.philhosoft.mif.parser;

import org.philhosoft.mif.model.MifFileContent;


/*
VERSION n
Charset �characterSetName�
[ DELIMITER �<c>� ]
[ UNIQUE n,n... ]
[ INDEX n,n... ]
[ COORDSYS... ]
[ TRANSFORM... ]
COLUMNS n
  <name> <type>
  <name> <type>
  .
  .
DATA
*/
public class MifHeaderParser
{
	private static final String[] HEADERS = { "DELIMITER", "UNIQUE", "INDEX", "COORDSYS", "TRANSFORM" };

	/**
	 * Parses the header, leaving the reader after the DATA line.
	 *
	 * @param reader  Mif file reader
	 * @return a MifFileContent with the header fields filled
	 */
	public MifFileContent parse(MifReader reader)
	{
		MifHeaderLineParser lineParser = new MifHeaderLineParser();
		MifFileContent fc = new MifFileContent();
		fc.setVersion(lineParser.parse("VERSION", reader));
		fc.setCharset(lineParser.parse("CHARSET", reader));

		// Tries to parse each kind of optional header
		int i = 0;
		boolean successful = true;
		while (reader.readNextLine() && successful)
		{
			String value = lineParser.parse(HEADERS[i++], reader);
			successful = value != null;
		}

		String columnNbParam = lineParser.parse("COLUMNS", reader);
		if (columnNbParam == null)
		{
			reader.addError("COLUMNS field not found");
			return fc;
		}
		int columnNb = 0;
		try
		{
			columnNb = Integer.parseInt(columnNbParam);
		}
		catch (NumberFormatException e)
		{
			reader.addError("Incorrect COLUMNS parameter");
			return fc;
		}
		for (int c = 0; c < columnNb; c++)
		{
			reader.readNextLine(); // Skip these lines for now?
		}

		reader.readNextLine();
		String data = reader.getCurrentLine();
		if (!data.equals("DATA"))
		{
			reader.addError("No DATA line found");
			return fc;
		}

		return fc;
	}
}