package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Asesoria;

public class Observador implements Observer {

    private Asesoria asesoria;

    public Observador(Asesoria asesoria) {
        this.asesoria = asesoria;
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        ArrayList<Object> resultado = (ArrayList<Object>) value;
        if (resultado.get(0).equals("Cliente")) {
            asesoria.cliente.reorganizarCliente(resultado.get(1).toString());
            asesoria.cliente.guardarCliente(asesoria.getId());
        }
    }

    public Asesoria retornarAsesoria() {
        return asesoria;
    }

}
