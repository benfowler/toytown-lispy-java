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

package au.id.bjf.toylisp;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Lexer {

	private static final String DOT = ".";
	private static final String TRUE = "#t";
	private static final String FALSE = "#f";

	private final List<String> tokens;
	private int index;

	public Lexer(final String program) {
		tokens = tokenize(program);
		index = 0;
	}

	static List<String> tokenize(String program) {
		program = program.replaceAll("\\(", " ( ")
				         .replaceAll("\\)", " ) ");
		final List<String> result = new LinkedList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(program);
		while (tokenizer.hasMoreTokens()) {
			result.add(tokenizer.nextToken());
		}
		return result;
	}

	public int size() {
		return tokens.size();
	}

	public boolean hasMoreTokens() {
		return (index < tokens.size());
	}

	public Object peekToken() throws ParseException {
		if (!hasMoreTokens()) {
			throw new ParseException(Errors.PREMATURE_END_OF_PROGRAM);
		}
		final String input = tokens.get(index);
		if (("(".equals(input)) || ")".equals(input))
		 {
			return input;  // as is
		}
		return toAtom(input);
	}

	public Object consumeToken() throws ParseException {
		if (!hasMoreTokens()) {
			throw new ParseException(Errors.PREMATURE_END_OF_PROGRAM);
		}
		final String input = tokens.get(index++);
		if (("(".equals(input)) || ")".equals(input))
		 {
			return input;  // as is
		}
		return toAtom(input);
	}

	public Object toAtom(final String input) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException ignore) { }

		if (TRUE.equals(input)) {
			return Boolean.TRUE;
		} else if (FALSE.equals(input)) {
			return Boolean.FALSE;
		} else if (input.startsWith(DOT)) {
			return new Dot(input.substring(1, input.length()));
		} else {
			SpecialForm sf = SpecialForm.getByLiteralVal(input);
			return (sf != null ? sf : new Symbol(input));
		}
	}

	public void expect(final Object token) throws ParseException {
		Object actual = consumeToken();
		if (!actual.equals(token)) {
			throw new ParseException(Errors.UNEXPECTED_TOKEN, token, actual);
		}
	}

	public void print(final PrintStream out) {
		String separator = "";
		for (String token : tokens) {
			out.print(separator + " \"" + token + "\"");
			separator = ",";
		}
	}

}
