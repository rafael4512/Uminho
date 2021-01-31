/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 11 23:34:34 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class Encomenda_ESTest extends Encomenda_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = encomenda0.clone();
      encomenda1.setPeso((-2172.34));
      boolean boolean0 = encomenda1.equals(encomenda0);
      assertEquals((-2172.34), encomenda1.getPeso(), 0.01);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertFalse(encomenda0.isPreparada());
      
      encomenda0.setPreparada(true);
      boolean boolean0 = encomenda0.isPreparada();
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.isLevantada();
      assertEquals(" ", encomenda0.getComprador());
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isPreparada());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(boolean0);
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LocalDateTime localDateTime0 = MockLocalDateTime.now();
      Encomenda encomenda0 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, (String) null, hashMap0, false, localDateTime0, true, true, true);
      boolean boolean0 = encomenda0.isEntregue();
      assertTrue(encomenda0.isPreparada());
      assertTrue(encomenda0.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertTrue(boolean0);
      assertFalse(encomenda0.isEncomendaMedica());
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertTrue(encomenda0.isEncomendaMedica());
      
      encomenda0.setEncomendaMedica(false);
      boolean boolean0 = encomenda0.isEncomendaMedica();
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      String string0 = encomenda0.getVendedor();
      assertFalse(encomenda0.isPreparada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", string0);
      assertFalse(encomenda0.isLevantada());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getCodigo_user());
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LinhaEncomenda linhaEncomenda0 = new LinhaEncomenda(":vpOV#WP7'XatiNdGq", ", _tO/t xg039LH", (-59.6263), (-59.6263));
      BiFunction<Object, Object, LinhaEncomenda> biFunction0 = (BiFunction<Object, Object, LinhaEncomenda>) mock(BiFunction.class, new ViolatedAssumptionAnswer());
      hashMap0.merge(":vpOV#WP7'XatiNdGq", linhaEncomenda0, biFunction0);
      encomenda0.setProdutos(hashMap0);
      encomenda0.getProdutos();
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isEntregue());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getVendedor());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      encomenda0.setPeso(2909.4192);
      double double0 = encomenda0.getPeso();
      assertEquals(2909.4192, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Map<String, LinhaEncomenda> map0 = encomenda0.getProdutos();
      Encomenda encomenda1 = new Encomenda("R-_vyR`G<H}", "UzlHu&2t", (String) null, (-5177L), "R-_vyR`G<H}", (String) null, map0, false, (LocalDateTime) null, false, false, false);
      double double0 = encomenda1.getPeso();
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getVendedor());
      assertFalse(encomenda0.isLevantada());
      assertEquals((-5177.0), double0, 0.01);
      assertFalse(encomenda1.isEntregue());
      assertFalse(encomenda1.isPreparada());
      assertFalse(encomenda1.isEncomendaMedica());
      assertFalse(encomenda0.isEntregue());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda1.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      encomenda0.setData((LocalDateTime) null);
      encomenda0.getData();
      assertFalse(encomenda0.isLevantada());
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isPreparada());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LocalDateTime localDateTime0 = MockLocalDateTime.now();
      Encomenda encomenda0 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, (String) null, hashMap0, false, localDateTime0, true, true, true);
      encomenda0.getComprador();
      assertTrue(encomenda0.isPreparada());
      assertTrue(encomenda0.isEntregue());
      assertFalse(encomenda0.isEncomendaMedica());
      assertTrue(encomenda0.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertEquals(" ", encomenda0.getComprador());
      
      encomenda0.setComprador("");
      encomenda0.getComprador();
      assertEquals(" ", encomenda0.getCodigo());
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LocalDateTime localDateTime0 = MockLocalDateTime.now();
      Encomenda encomenda0 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, (String) null, hashMap0, false, localDateTime0, true, true, true);
      encomenda0.getCodigo_user();
      assertTrue(encomenda0.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertTrue(encomenda0.isEntregue());
      assertTrue(encomenda0.isPreparada());
      assertFalse(encomenda0.isEncomendaMedica());
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      Encomenda encomenda0 = new Encomenda("", "", "", 1.0, "Alcool", "Desinfetante", hashMap0, false, (LocalDateTime) null, false, true, false);
      encomenda0.getCodigo_user();
      assertFalse(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isEntregue());
      assertFalse(encomenda0.isPreparada());
      assertTrue(encomenda0.isLevantada());
      assertEquals(1.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Map<String, LinhaEncomenda> map0 = encomenda0.getProdutos();
      Encomenda encomenda1 = new Encomenda("R-_vyR`G<H}", "UzlHu&2t", (String) null, (-5177L), "R-_vyR`G<H}", (String) null, map0, false, (LocalDateTime) null, false, false, false);
      encomenda1.getCodigo_loja();
      assertFalse(encomenda0.isLevantada());
      assertFalse(encomenda1.isEntregue());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertFalse(encomenda1.isPreparada());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda1.isEncomendaMedica());
      assertFalse(encomenda0.isPreparada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getCodigo_user());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals((-5177.0), encomenda1.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getComprador());
      assertFalse(encomenda1.isLevantada());
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertEquals(" ", encomenda0.getCodigo_loja());
      
      encomenda0.setCodigo_loja("");
      encomenda0.getCodigo_loja();
      assertEquals(" ", encomenda0.getCodigo());
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Map<String, LinhaEncomenda> map0 = encomenda0.getProdutos();
      Encomenda encomenda1 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, "Z7x!/iA", map0, true, (LocalDateTime) null, false, false, false);
      encomenda1.getCodigo();
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda1.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getVendedor());
      assertTrue(encomenda1.isEncomendaMedica());
      assertFalse(encomenda1.isEntregue());
      assertFalse(encomenda0.isLevantada());
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
      assertFalse(encomenda0.isEntregue());
      assertFalse(encomenda1.isPreparada());
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertEquals(" ", encomenda0.getCodigo());
      
      encomenda0.setCodigo("");
      encomenda0.getCodigo();
      assertTrue(encomenda0.isEncomendaMedica());
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertFalse(encomenda0.isLevantada());
      
      encomenda0.setLevantada(true);
      encomenda0.clone();
      assertTrue(encomenda0.isLevantada());
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertFalse(encomenda0.isEntregue());
      
      encomenda0.setEntregue(true);
      encomenda0.clone();
      assertTrue(encomenda0.isEntregue());
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LocalDateTime localDateTime0 = MockLocalDateTime.now();
      Encomenda encomenda0 = new Encomenda("Peso: ", "Peso: ", "", 1.0, "C\u00F3digo de produto: ", "D7qEh", hashMap0, true, localDateTime0, false, false, true);
      Encomenda encomenda1 = encomenda0.clone();
      assertTrue(encomenda1.isEncomendaMedica());
      assertTrue(encomenda1.isPreparada());
      assertFalse(encomenda1.isLevantada());
      assertFalse(encomenda1.isEntregue());
      assertEquals(1.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      encomenda0.setPeso((-1691.0891287192699));
      encomenda0.clone();
      assertEquals((-1691.0891287192699), encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test22()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      // Undeclared exception!
      try { 
        encomenda0.setProdutos((Map<String, LinhaEncomenda>) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("Encomenda", e);
      }
  }

  @Test(timeout = 4000)
  public void test23()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = encomenda0.clone();
      encomenda1.setCodigo_user((String) null);
      // Undeclared exception!
      try { 
        encomenda1.equals(encomenda0);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
      }
  }

  @Test(timeout = 4000)
  public void test24()  throws Throwable  {
      Encomenda encomenda0 = null;
      try {
        encomenda0 = new Encomenda("\n", "C\u00F3digo: ", "<-;f)~MM&27F ", (-3552.46840287421), "<-;f)~MM&27F ", "<-;f)~MM&27F ", (Map<String, LinhaEncomenda>) null, false, (LocalDateTime) null, false, false, false);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("Encomenda", e);
      }
  }

  @Test(timeout = 4000)
  public void test25()  throws Throwable  {
      Encomenda encomenda0 = null;
      try {
        encomenda0 = new Encomenda((Encomenda) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("Encomenda", e);
      }
  }

  @Test(timeout = 4000)
  public void test26()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      Encomenda encomenda0 = new Encomenda("", "", "", 1.0, "Alcool", "Desinfetante", hashMap0, false, (LocalDateTime) null, false, true, false);
      boolean boolean0 = encomenda0.isLevantada();
      assertFalse(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isPreparada());
      assertTrue(boolean0);
      assertFalse(encomenda0.isEntregue());
      assertEquals(1.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test27()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.isPreparada();
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(boolean0);
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getVendedor());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getComprador());
      assertFalse(encomenda0.isLevantada());
  }

  @Test(timeout = 4000)
  public void test28()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      encomenda0.getData();
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getComprador());
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getCodigo());
  }

  @Test(timeout = 4000)
  public void test29()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      Encomenda encomenda0 = new Encomenda("", "", "", 1.0, "Alcool", "Desinfetante", hashMap0, false, (LocalDateTime) null, false, true, false);
      encomenda0.getComprador();
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isEntregue());
      assertEquals(1.0, encomenda0.getPeso(), 0.01);
      assertTrue(encomenda0.isLevantada());
  }

  @Test(timeout = 4000)
  public void test30()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.isEncomendaMedica();
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertFalse(encomenda0.isLevantada());
      assertTrue(boolean0);
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isPreparada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getComprador());
  }

  @Test(timeout = 4000)
  public void test31()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      String string0 = encomenda0.getCodigo_loja();
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isPreparada());
      assertEquals(" ", string0);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isLevantada());
      assertTrue(encomenda0.isEncomendaMedica());
  }

  @Test(timeout = 4000)
  public void test32()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.isEntregue();
      assertEquals(" ", encomenda0.getVendedor());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(boolean0);
      assertFalse(encomenda0.isPreparada());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getCodigo());
  }

  @Test(timeout = 4000)
  public void test33()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      double double0 = encomenda0.getPeso();
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getCodigo());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertEquals(" ", encomenda0.getVendedor());
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isEntregue());
      assertFalse(encomenda0.isLevantada());
      assertEquals(0.0, double0, 0.01);
      assertEquals(" ", encomenda0.getComprador());
      assertFalse(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test34()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      String string0 = encomenda0.getCodigo_user();
      assertFalse(encomenda0.isLevantada());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", string0);
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test35()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      String string0 = encomenda0.getCodigo();
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", string0);
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda0.isEntregue());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getVendedor());
  }

  @Test(timeout = 4000)
  public void test36()  throws Throwable  {
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LocalDateTime localDateTime0 = MockLocalDateTime.now();
      Encomenda encomenda0 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, (String) null, hashMap0, false, localDateTime0, true, true, true);
      encomenda0.getVendedor();
      assertTrue(encomenda0.isLevantada());
      assertTrue(encomenda0.isEntregue());
      assertFalse(encomenda0.isEncomendaMedica());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertTrue(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test37()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = new Encomenda(encomenda0);
      encomenda1.setEncomendaMedica(false);
      boolean boolean0 = encomenda0.equals(encomenda1);
      assertFalse(encomenda1.isEncomendaMedica());
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test38()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      HashMap<String, LinhaEncomenda> hashMap0 = new HashMap<String, LinhaEncomenda>();
      LinhaEncomenda linhaEncomenda0 = new LinhaEncomenda();
      hashMap0.put("YS*6@4=rNv}?s~onK", linhaEncomenda0);
      encomenda0.setProdutos(hashMap0);
      Encomenda encomenda1 = new Encomenda();
      boolean boolean0 = encomenda0.equals(encomenda1);
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
      assertFalse(encomenda1.isPreparada());
      assertFalse(encomenda1.isEntregue());
      assertEquals(" ", encomenda1.getCodigo_user());
      assertFalse(encomenda1.isLevantada());
      assertTrue(encomenda1.isEncomendaMedica());
      assertEquals(" ", encomenda1.getCodigo());
      assertEquals(" ", encomenda1.getVendedor());
      assertEquals(" ", encomenda1.getCodigo_loja());
      assertFalse(boolean0);
      assertEquals(" ", encomenda1.getComprador());
  }

  @Test(timeout = 4000)
  public void test39()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = new Encomenda(encomenda0);
      assertEquals(" ", encomenda0.getVendedor());
      
      encomenda0.setVendedor("");
      boolean boolean0 = encomenda1.equals(encomenda0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test40()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = new Encomenda();
      assertTrue(encomenda1.equals((Object)encomenda0));
      
      encomenda1.setComprador((String) null);
      boolean boolean0 = encomenda0.equals(encomenda1);
      assertFalse(boolean0);
      assertEquals(" ", encomenda1.getCodigo_user());
      assertFalse(encomenda1.isPreparada());
      assertFalse(encomenda1.isEntregue());
      assertEquals(" ", encomenda1.getCodigo());
      assertFalse(encomenda1.isLevantada());
      assertTrue(encomenda1.isEncomendaMedica());
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda1.getVendedor());
      assertEquals(" ", encomenda1.getCodigo_loja());
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test41()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertEquals(" ", encomenda0.getCodigo_user());
      
      encomenda0.setCodigo_user("Quantidade: ");
      Encomenda encomenda1 = new Encomenda();
      boolean boolean0 = encomenda0.equals(encomenda1);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test42()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = new Encomenda();
      boolean boolean0 = encomenda1.equals(encomenda0);
      assertTrue(boolean0);
      assertEquals(" ", encomenda1.getComprador());
      assertTrue(encomenda1.isEncomendaMedica());
      assertEquals(" ", encomenda1.getVendedor());
      assertFalse(encomenda1.isLevantada());
      assertEquals(" ", encomenda1.getCodigo());
      assertEquals(" ", encomenda1.getCodigo_user());
      assertFalse(encomenda1.isPreparada());
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
      assertFalse(encomenda1.isEntregue());
      assertEquals(" ", encomenda1.getCodigo_loja());
  }

  @Test(timeout = 4000)
  public void test43()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.equals((Object) null);
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(boolean0);
      assertEquals(" ", encomenda0.getComprador());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda0.isLevantada());
  }

  @Test(timeout = 4000)
  public void test44()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.equals(encomenda0);
      assertEquals(" ", encomenda0.getCodigo_user());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertTrue(boolean0);
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getComprador());
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test45()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      boolean boolean0 = encomenda0.equals("\n");
      assertFalse(encomenda0.isLevantada());
      assertEquals(" ", encomenda0.getComprador());
      assertTrue(encomenda0.isEncomendaMedica());
      assertEquals(" ", encomenda0.getCodigo());
      assertFalse(encomenda0.isPreparada());
      assertFalse(boolean0);
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(encomenda0.isEntregue());
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
  }

  @Test(timeout = 4000)
  public void test46()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertTrue(encomenda0.isEncomendaMedica());
      
      encomenda0.setEncomendaMedica(false);
      encomenda0.clone();
      assertFalse(encomenda0.isEncomendaMedica());
  }

  @Test(timeout = 4000)
  public void test47()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      String string0 = encomenda0.toString();
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda0.isLevantada());
      assertEquals("\nEncomenda: \nC\u00F3digo:  \nC\u00F3digo do utilizador:  \nC\u00F3digo da loja:  \nPeso: 0.0\nComprador:  \nVendedor:  \nData de emiss\u00E3o da encomenda: 2014-02-14T20:21:21.320\nProdutos: \n{}\n", string0);
      assertFalse(encomenda0.isEntregue());
      assertFalse(encomenda0.isPreparada());
  }

  @Test(timeout = 4000)
  public void test48()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      assertEquals(" ", encomenda0.getVendedor());
      
      encomenda0.setVendedor("");
      encomenda0.getVendedor();
      assertFalse(encomenda0.isLevantada());
  }

  @Test(timeout = 4000)
  public void test49()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Map<String, LinhaEncomenda> map0 = encomenda0.getProdutos();
      Encomenda encomenda1 = new Encomenda((String) null, (String) null, (String) null, 0.0, (String) null, "Z7x!/iA", map0, true, (LocalDateTime) null, false, false, false);
      boolean boolean0 = encomenda0.equals(encomenda1);
      assertEquals(" ", encomenda0.getCodigo_loja());
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
      assertEquals(" ", encomenda0.getComprador());
      assertFalse(encomenda0.isLevantada());
      assertEquals(0.0, encomenda0.getPeso(), 0.01);
      assertFalse(encomenda1.isLevantada());
      assertEquals(" ", encomenda0.getVendedor());
      assertEquals(" ", encomenda0.getCodigo_user());
      assertFalse(boolean0);
      assertFalse(encomenda0.isEntregue());
      assertFalse(encomenda0.isPreparada());
      assertFalse(encomenda1.isEntregue());
      assertTrue(encomenda1.isEncomendaMedica());
      assertEquals(" ", encomenda0.getCodigo());
      assertTrue(encomenda0.isEncomendaMedica());
      assertFalse(encomenda1.isPreparada());
  }

  @Test(timeout = 4000)
  public void test50()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = encomenda0.clone();
      assertEquals(0.0, encomenda1.getPeso(), 0.01);
      
      encomenda1.setPeso(1.0);
      boolean boolean0 = encomenda1.equals(encomenda0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void test51()  throws Throwable  {
      Encomenda encomenda0 = new Encomenda();
      Encomenda encomenda1 = new Encomenda(encomenda0);
      assertEquals(" ", encomenda1.getCodigo_loja());
      
      encomenda1.setCodigo_loja("");
      boolean boolean0 = encomenda1.equals(encomenda0);
      assertFalse(boolean0);
  }
}
