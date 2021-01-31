import java.io.Serializable;

public class LinhaEncomenda implements Serializable {
    private String codigo;
    private String descricao;
    private double quantidade;
    private double preco;

    public LinhaEncomenda(){
        this.codigo = " ";
        this.descricao = " ";
        this.preco = 0;
        this.quantidade = 0;
    }

    public LinhaEncomenda(String codigo, String descricao, double quantidade, double  preco){
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public LinhaEncomenda(LinhaEncomenda a){
        this.codigo = a.getCodigo();
        this.descricao = a.getDescricao();
        this.preco = a.getPreco();
        this.quantidade = a.getQuantidade();
    }

    public String getCodigo(){
        return this.codigo;
    }

    public double getPreco() {
        return this.preco;
    }

    public double getQuantidade() {
        return this.quantidade;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public LinhaEncomenda clone(){
        return new LinhaEncomenda(this);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
			return true;
		}
        if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
        LinhaEncomenda o = (LinhaEncomenda) obj;

        return this.codigo.equals(o.getCodigo()) &&
                this.descricao.equals(o.getDescricao()) &&
                this.preco == o.getPreco() &&
                this.quantidade == o.getQuantidade();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Código de produto: ");
        sb.append(this.codigo).append("\n");
        sb.append("Produto: ");
        sb.append(this.descricao).append("\n");
        sb.append("Quantidade: ");
        sb.append(this.quantidade).append("\n");
        sb.append("Preço: ");
        sb.append(this.preco).append("\n");

        return sb.toString();
    }

    public boolean isMed(){
        return "Alcool".equals(descricao) || "Desinfetante".equals(descricao) || "Saco de lixo 30l".equals(descricao) || "Saco de lixo de 50l".equals(descricao);
    }
}
