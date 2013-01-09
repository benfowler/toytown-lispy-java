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

import static au.id.bjf.toylisp.SpecialForm.DEFINE;
import static au.id.bjf.toylisp.SpecialForm.IF;
import static au.id.bjf.toylisp.SpecialForm.LAMBDA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

public class TestParser {

	private static final String TEST_PRG_FACT =
			"(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))";

	private static final String TEST_PRG_TOO_MANY_TOKENS = "(fred 1 2 ))";

	private static final String TEST_PRG_MISSING_PARENTHESIS = "(fred 1 2";


	private static final Object[] TEST_PRG_FACT_TOKENS =
		{ "(", DEFINE, sym("fact"), "(", LAMBDA, "(", sym("n"),
		")", "(", IF, "(", sym("<="), sym("n"), 1., ")", 1., "(",
		sym("*"), sym("n"), "(", sym("fact"), "(", sym("-"), sym("n"), 1.,
		")", ")", ")", ")", ")", ")" };


	@Test
	public void testSmokeTest() {
		final Lexer lexer = new Lexer(TEST_PRG_FACT);
		lexer.print(System.out);
		System.out.println();
		assertTrue("no tokens", lexer.size() > 0);
	}

	@Test
	public void testTokenizePeekConsume() throws ParseException {
		final Lexer lexer = new Lexer(TEST_PRG_FACT);
		for (Object expectedToken : TEST_PRG_FACT_TOKENS) {
			assertEquals("Token doesn't match expected", expectedToken,
					lexer.peekToken());
			lexer.consumeToken();
		}
		assertTrue("Unconsumed tokens", !lexer.hasMoreTokens());
	}

	@Test
	public void testTokenizeExpect() throws ParseException {
		final Lexer lexer = new Lexer(TEST_PRG_FACT);
		for (Object expectedToken : TEST_PRG_FACT_TOKENS) {
			lexer.expect(expectedToken);
		}
		assertTrue("Unconsumed tokens", !lexer.hasMoreTokens());
	}

	@Test
	public void testParserSmokeTest() throws ParseException {
		final Object parse = Interpreter.parse(TEST_PRG_FACT);
		System.out.println(parse);
		assertTrue("No s-expr returned", parse != null);
		assertTrue("No s-expr returned", parse instanceof List);
		final List<?> l = (List<?>)parse;
		assertTrue("No s-expr returned", l.size() > 0);
	}

	@Test
	public void testParserExtraParens() throws ParseException {
		try {
			Interpreter.parse(TEST_PRG_TOO_MANY_TOKENS);
			fail("ParseException should have been thrown.");
		} catch (ParseException pe) {
			assertTrue(pe.getError().equals(Errors.TOKEN_AFTER_END_OF_PROGRAM));
		}
	}

	@Test
	public void testParserMissingParens() throws ParseException {
		try {
			Interpreter.parse(TEST_PRG_MISSING_PARENTHESIS);
			fail("ParseException should have been thrown.");
		} catch (ParseException pe) {
			assertTrue(pe.getError().equals(Errors.PREMATURE_END_OF_PROGRAM));
		}
	}

	private static Symbol sym(final String lexeme) {
		return new Symbol(lexeme);
	}

}
