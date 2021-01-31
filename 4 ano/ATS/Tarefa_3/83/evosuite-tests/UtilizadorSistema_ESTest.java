/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 11 23:31:28 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class UtilizadorSistema_ESTest extends UtilizadorSistema_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      ArrayList<Encomenda> arrayList0 = new ArrayList<Encomenda>();
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes("Q6@", (String) null, "fgZHPo;J{", (String) null, (-1), 0.2, " <--- O utilizador \u00E9: \n", 0.0, 94, 0.2, arrayList0, false, 0.2, (-707), false, 2147483645, (-1));
      empresaTransportes0.getTypeUser();
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(94.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      empresaTransportes0.setPassword("70");
      empresaTransportes0.getPassword();
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      ArrayList<Encomenda> arrayList0 = empresaTransportes0.getRota();
      EmpresaTransportes empresaTransportes1 = new EmpresaTransportes("db?50b)I,9RQgMy", "q", "", "V\"g?#tL8%T", (-2138387410), 0.0, "V\"g?#tL8%T", 0.0, 1904.58804, 0.0, arrayList0, true, (-2470.37231766), (-2034458190), true, (-2034458190), (-2054397838));
      empresaTransportes1.getNome();
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes1.getLatitude(), 0.01);
      assertEquals(1904.58804, empresaTransportes1.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.getNome();
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      double double0 = empresaTransportes0.getLongitude();
      assertEquals(0.0, double0, 0.01);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      Loja loja0 = new Loja();
      ArrayList<Encomenda> arrayList0 = loja0.getEncomendas_recebidas();
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes("\n", "\n", "Sz#65(eZd$5u", "2)!Zo]?)#{@", (-174), 52.167254015, "2)!Zo]?)#{@", 52.167254015, 52.167254015, (-1.0), arrayList0, false, (-1.0), 1515, false, (-2900), (-174));
      double double0 = empresaTransportes0.getLongitude();
      assertEquals(52.167254015, double0, 0.01);
      assertEquals(52.167254015, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, loja0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      double double0 = empresaTransportes0.getLatitude();
      assertEquals(0.0, double0, 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      ArrayList<Encomenda> arrayList0 = empresaTransportes0.getRegistos();
      EmpresaTransportes empresaTransportes1 = new EmpresaTransportes("%Qv~N:o0W8", " <--- O utilizador \u00E9: \n", "!e);B()n", " <--- O utilizador \u00E9: \n", 193, 193, "$Xb", 193, 193, 193, arrayList0, true, 193, (-1073741822), true, (-1073741822), (-2034458190));
      double double0 = empresaTransportes1.getLatitude();
      assertEquals(193.0, double0, 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(193.0, empresaTransportes1.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      Loja loja0 = new Loja();
      loja0.getEmail();
      assertEquals(0.0, loja0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      ArrayList<Encomenda> arrayList0 = empresaTransportes0.getRota();
      Voluntario voluntario0 = new Voluntario("Q6I", (String) null, (String) null, (String) null, true, (-3202.2626403873), (-3202.2626403873), (LocalDate) null, 1908.173, arrayList0, (-1242.4901039554218), 2203, true, 94, 10);
      voluntario0.getCodigo();
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
      assertEquals((-3202.2626403873), voluntario0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals((-3202.2626403873), voluntario0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.getCodigo();
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.clone();
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      LocalDate localDate0 = MockLocalDate.ofEpochDay(0L);
      ArrayList<Encomenda> arrayList0 = new ArrayList<Encomenda>();
      Voluntario voluntario0 = new Voluntario("", "", "", "", false, 107.61, 1489.83844149766, localDate0, 1489.83844149766, arrayList0, 0.0, 1780, true, 1780, 1780);
      voluntario0.clone();
      assertEquals(107.61, voluntario0.getLatitude(), 0.01);
      assertEquals(1489.83844149766, voluntario0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      ArrayList<Encomenda> arrayList0 = empresaTransportes0.getRegistos();
      Voluntario voluntario0 = new Voluntario("b.q=av", "Produtos: \n", "Produtos: \n", "b.q=av", false, (-2008.4743611435), (-2008.4743611435), (LocalDate) null, (-2008.4743611435), arrayList0, (-2008.4743611435), (-1843), true, (-1843), (-1843));
      UtilizadorSistema utilizadorSistema0 = voluntario0.clone();
      assertEquals((-2008.4743611435), utilizadorSistema0.getLongitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
      assertEquals((-2008.4743611435), utilizadorSistema0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      ArrayList<Encomenda> arrayList0 = new ArrayList<Encomenda>();
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes(" 0/;_", " 0/;_", "3zt", " 0/;_", (-4952), (-4952), " 0/;_", 0.0, 0.0, (-1076.20795578915), arrayList0, true, 0.0, (-363), false, (-363), (-4952));
      empresaTransportes0.getEmail();
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.getPassword();
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      empresaTransportes0.getTypeUser();
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      EmpresaTransportes empresaTransportes1 = empresaTransportes0.clone();
      assertTrue(empresaTransportes1.equals((Object)empresaTransportes0));
      
      empresaTransportes1.setEmail("");
      boolean boolean0 = empresaTransportes0.equals(empresaTransportes1);
      assertFalse(empresaTransportes1.equals((Object)empresaTransportes0));
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      EmpresaTransportes empresaTransportes1 = empresaTransportes0.clone();
      assertTrue(empresaTransportes1.equals((Object)empresaTransportes0));
      
      empresaTransportes1.setTypeUser("");
      boolean boolean0 = empresaTransportes1.equals(empresaTransportes0);
      assertFalse(empresaTransportes1.equals((Object)empresaTransportes0));
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      empresaTransportes0.setPassword("m");
      EmpresaTransportes empresaTransportes1 = new EmpresaTransportes();
      boolean boolean0 = empresaTransportes1.equals(empresaTransportes0);
      assertEquals(0.0, empresaTransportes1.getLatitude(), 0.01);
      assertFalse(boolean0);
      assertEquals(0.0, empresaTransportes1.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      Utilizador utilizador0 = new Utilizador();
      Object object0 = new Object();
      boolean boolean0 = utilizador0.equals(object0);
      assertEquals(0.0, utilizador0.getLongitude(), 0.01);
      assertFalse(boolean0);
      assertEquals(0.0, utilizador0.getLatitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      Utilizador utilizador0 = new Utilizador();
      boolean boolean0 = utilizador0.equals((Object) null);
      assertFalse(boolean0);
      assertEquals(0.0, utilizador0.getLatitude(), 0.01);
      assertEquals(0.0, utilizador0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test22()  throws Throwable  {
      Utilizador utilizador0 = new Utilizador();
      boolean boolean0 = utilizador0.equals(utilizador0);
      assertTrue(boolean0);
      assertEquals(0.0, utilizador0.getLatitude(), 0.01);
      assertEquals(0.0, utilizador0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test23()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.setCodigo(" <--- O utilizador \u00E9: \n");
      voluntario0.getCodigo();
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test24()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      String string0 = voluntario0.toString();
      assertEquals(" <--- O utilizador \u00E9: \n\n\nNome: \nC\u00F3digo de volunt\u00E1rio: \nDispon\u00EDvel: false\nLatitude: 0.0\nLongitude: 0.0\nHora de registo: 2014-02-14\nRaio de a\u00E7\u00E3o: 0.0\nRegistos de encomendas: []", string0);
  }

  @Test(timeout = 4000)
  public void test25()  throws Throwable  {
      Utilizador utilizador0 = new Utilizador();
      utilizador0.setLatitude((-1241.6270422348691));
      double double0 = utilizador0.getLatitude();
      assertEquals((-1241.6270422348691), utilizador0.getLatitude(), 0.01);
      assertEquals((-1241.6270422348691), double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test26()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.setLongitude((-4266.082308));
      assertEquals((-4266.082308), voluntario0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test27()  throws Throwable  {
      EmpresaTransportes empresaTransportes0 = new EmpresaTransportes();
      EmpresaTransportes empresaTransportes1 = empresaTransportes0.clone();
      boolean boolean0 = empresaTransportes0.equals(empresaTransportes1);
      assertEquals(0.0, empresaTransportes0.getLatitude(), 0.01);
      assertTrue(boolean0);
      assertEquals(0.0, empresaTransportes0.getLongitude(), 0.01);
  }

  @Test(timeout = 4000)
  public void test28()  throws Throwable  {
      Voluntario voluntario0 = new Voluntario();
      voluntario0.setNome((String) null);
      assertEquals(0.0, voluntario0.getLatitude(), 0.01);
      assertEquals(0.0, voluntario0.getLongitude(), 0.01);
  }
}