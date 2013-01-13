/*
 *  Toytown -- a reimplementation of Peter Norvig's lis.py interpreter for
 *  Python.
 *
 *  Copyright (C) 2012-2013  Benjamin J. Fowler.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package benfowler.toytown.lispy;

import static benfowler.toytown.lispy.Interpreter.eval;
import static benfowler.toytown.lispy.Interpreter.read;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;


public abstract class AbstractExternalFileTest {

	private String readFile(final String path) throws IOException {
		final InputStream stream = getClass().getClassLoader()
				.getResourceAsStream(path);
		final BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		final StringBuilder builder = new StringBuilder();

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} finally {
			stream.close();
		}

		return builder.toString();
	}

	@Test
	public void test() throws EvalException, IOException {
		final String program = readFile(getPathToScriptResource());
		Object o = eval(read(program));
		assertTrue(o != null);
		assertEquals(getTestResult(), o);
	}

	protected abstract String getPathToScriptResource();

	protected abstract Object getTestResult();

}
