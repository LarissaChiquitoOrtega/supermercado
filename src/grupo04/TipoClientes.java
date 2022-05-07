package grupo04;

public enum TipoClientes {
    FISICA("PF", 1),
    JURIDICA("PJ", 0.95),
    VIP("VIP", 0.85);
    private double desconto;
    private String tipo;
    TipoClientes(String descricao, double desconto){
        this.tipo = descricao;
        this.desconto = desconto;
    }

    public String getTipo() {
        return this.tipo;
    }

    public double getDesconto() {
        return this.desconto;
    }
}
