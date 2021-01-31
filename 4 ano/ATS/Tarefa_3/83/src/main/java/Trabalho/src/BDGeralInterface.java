import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

public interface BDGeralInterface {
    BDLojas getLojas();
    EncomendasAceites getEncomendasAceites();
    BDTransportes getTransportes();
    BDUtilizador getUtilizadores();
    BDVoluntarios getVoluntarios();
    BDProdutos getProdutos();
    void setEncomendasAceites(EncomendasAceites encomendasAceites);
    void addVoluntario(Voluntario v);
    void addVoluntarioDisponivel(Voluntario v);
    void addUser(Utilizador u);
    void addTransporte(EmpresaTransportes t);
    void addLoja(Loja l);
    void addProduto(LinhaEncomenda p);
    void updateLoja(Encomenda e, Loja j);
    void updateLoja2(Encomenda e ,Loja j);
    void updateUser(Encomenda e, Utilizador j);
    void updateVoluntario(Double classificacao, Voluntario v);
    void updateVoluntario2(Voluntario v);
    void updateAceites(String cod);
    void updateTransportes(Double classificacao, EmpresaTransportes e);
    void updateTransportes2(EmpresaTransportes e);
    Utilizador loginUser(String email, String password) throws UserNotFoundException;
    Voluntario loginVoluntario(String email, String password) throws VoluntarioNotFoundException;
    Loja loginLoja(String email, String password) throws LojaNotFoundException;
    EmpresaTransportes loginEmpresa(String email, String password) throws TransporteNotFoundException;
    void gravarFicheiro(String filename) throws IOException, FileNotFoundException, IOException;
    BDGeral lerFicheiro(String filename) throws IOException, ClassNotFoundException;
    Set<Pair> top10Encomendas();
}
