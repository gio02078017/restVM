package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import javax.crypto.NullCipher;

import co.com.une.appmovilesune.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    public static final String NOMBRE = "ventamovil";
    public static final int VERSION = 1;

	private String estructura[] = new String[40];

    public BaseDatos(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        definirEstructura();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < estructura.length; i++) {
            db.execSQL("PRAGMA encoding =\"UTF-8\"");
            db.execSQL(estructura[i]);
        }
        MainActivity.config.reinicializarEstados();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        for (int i = 0; i < estructura.length; i++) {
            db.execSQL("PRAGMA encoding =\"UTF-8\"");
            db.execSQL(estructura[i]);
        }
        MainActivity.config.reinicializarEstados();
    }

	private void definirEstructura() {
		estructura[0] = "CREATE TABLE IF NOT EXISTS usuario(Codigo_Asesor TEXT PRIMARY KEY, Documento TEXT NOT NULL, Celular TEXT NOT NULL, Codigo_Canal TEXT NOT NULL, Nombre_Asesor TEXT NOT NULL, Departamento TEXT NOT NULL, Fecha DATE, Hora TIME)";
		estructura[1] = "CREATE TABLE IF NOT EXISTS Departamentos (codigo INTEGER PRIMARY KEY AUTOINCREMENT,Departamento TEXT,DepartamentoFenix TEXT,Ciudad TEXT,CiudadFenix TEXT,Codigo_Giis TEXT, Codigo_Amc TEXT, Identificador TEXT,CodigoDaneMunicipio TEXT,CodigoDaneDepartamento TEXT,Estado INTEGER, visible INTEGER)";
		estructura[2] = "CREATE TABLE IF NOT EXISTS peticiones(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,Transaccion TEXT,Cual TEXT,Productos TEXT,Evento TEXT,Pedido TEXT,Cun TEXT)";
		estructura[3] = "CREATE TABLE IF NOT EXISTS actualizacion(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,AtencionUNE TEXT,AtensionAsesor TEXT,Mensajes TEXT,mail TEXT,Observaciones TEXT,Fecha DATE,Hora TIME)";
		estructura[4] = "CREATE TABLE IF NOT EXISTS obsequios(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,Obsequios TEXT,Cual TEXT,Contrato TEXT,NumeroSIM TEXT,AtencionUNE TEXT,AtensionAsesor TEXT,Observaciones TEXT,Fecha DATE,Hora TIME)";
		estructura[5] = "CREATE TABLE IF NOT EXISTS asesorias(id INTEGER PRIMARY KEY AUTOINCREMENT,codigo_asesor TEXT,VisitaAtendida TEXT,Observaciones TEXT,FechaInicial TEXT,HoraInicial TEXT,FechaFinal TEXT,HoraFinal TEXT)";
		estructura[6] = "CREATE TABLE IF NOT EXISTS ventas(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,Observaciones TEXT,Documentacion TEXT,Telefonia TEXT,PrecioTelefonia TEXT,NuevoMigracionTO TEXT,PlanActualTO TEXT,PromoTO TEXT,TiempoPromoTO TEXT,PrecioPromoTO TEXT,Linea TEXT,PagoLinea TEXT,Television TEXT,PrecioTelevision TEXT,Extensiones TEXT,AdicionalTV TEXT,PreciosIndividualesAdicionalesTV TEXT,PrecioAdicionalTV TEXT,PromoTV TEXT,TiempoPromoTV TEXT,PrecioPromoTV TEXT,NuevoMigracionTV TEXT,PlanActualTV TEXT,Internet TEXT,PrecioInternet TEXT,PromoBA TEXT,TiempoPromoBA TEXT,PrecioPromoBA TEXT,Wifi TEXT,NuevoMigracionBA TEXT,PlanActualBA TEXT,InternetMovil TEXT,PrecioInternetMovil TEXT,NuevoMigracioIM TEXT,PlanActualIM TEXT,PromoIM TEXT,TiempoPromoIM TEXT,PrecioPromoIM TEXT,Modem TEXT,FinanciacionModem TEXT,PorcentajeModem TEXT,PrecioModem TEXT,EntregaModem TEXT,Internet4G TEXT,PrecioInternet4G TEXT,Promo4G TEXT,TiempoPromo4G TEXT,PrecioPromo4G TEXT,OtrasPromociones TEXT,Total TEXT,Empaquetamiento TEXT,Fecha TEXT,Hora TEXT,HorarioAtencion TEXT,Scooring TEXT)";
		estructura[7] = "CREATE TABLE IF NOT EXISTS competencias(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,Competencia TEXT,OtrasCompetencias TEXT,Productos TEXT,Empaquetado TEXT,VigenciaContrato DATE,PagoMensual REAL,NoUne TEXT,Observaciones TEXT,Fecha DATE,Hora TIME)";
		estructura[8] = "CREATE TABLE IF NOT EXISTS Promociones (Codigo INTEGER PRIMARY KEY,departamento TEXT,nombre TEXT,descripcion TEXT ,inicia TEXT,termina TEXT,aplica TEXT , activa TEXT)";
		estructura[9] = "CREATE TABLE IF NOT EXISTS Precios (Codigo INTEGER PRIMARY KEY,departamento TEXT,tipo_producto TEXT,Producto TEXT,ProductoHomologado TEXT ,estrato TEXT,tipo_paquete TEXT,individual INTEGER , individual_iva INTEGER,empaquetado INTEGER , empaquetado_iva INTEGER,Nuevo INTEGER,Oferta TEXT,cantidadproductos INTEGER,homoPrimeraLinea TEXT,homoSegundaLinea TEXT, valorGota TEXT,valorGotaIva TEXT,velocidadGota TEXT, tecnologia TEXT,idTarifa INTEGER, departamentoTarifa TEXT )";
		estructura[10] = "CREATE TABLE IF NOT EXISTS Productos (codigo INTEGER PRIMARY KEY AUTOINCREMENT,Departamento TEXT,Producto TEXT,ProductoHomologado TEXT ,Tipo_Producto TEXT,Nuevo INTEGER,Oferta TEXT,FechaCarga TEXT,Estrato TEXT,Tecnologia TEXT,idTarifa INTEGER, departamentoTarifa TEXT)";
		estructura[11] = "CREATE TABLE IF NOT EXISTS Descuentos (codigo INTEGER PRIMARY KEY AUTOINCREMENT,Ciudad TEXT,Descuentos Text,Tipo_Producto TEXT,Producto TEXT,Individual INTEGER,Individual_Iva INTEGER,Empaquetado INTEGER,Empaquetado_Iva INTEGER,Estrato Text)";
		estructura[12] = "CREATE TABLE IF NOT EXISTS Extensiones_HFC (codigo INTEGER PRIMARY KEY AUTOINCREMENT,Departamento TEXT,hfc TEXT,Producto TEXT)";
		estructura[13] = "CREATE TABLE IF NOT EXISTS Otras_Promociones (codigo INTEGER PRIMARY KEY AUTOINCREMENT,Departamento TEXT,Estrato Text, Precio INTEGER,Descripcion Text)";
		estructura[14] = "CREATE TABLE IF NOT EXISTS Adicionales (Codigo INTEGER PRIMARY KEY,departamento TEXT,tipoProducto TEXT, producto TEXT,adicional TEXT,tarifa INTEGER,tarifaIva INTEGER,estrato TEXT,plan TEXT,oferta TEXT,tecnologia TEXT)";
		estructura[15] = "CREATE TABLE IF NOT EXISTS Registro_Tarificador (Id INTEGER PRIMARY KEY AUTOINCREMENT,Codigo_Asesor TEXT, Fecha TEXT,Hora TEXT)";
		estructura[16] = "CREATE TABLE IF NOT EXISTS GPS (Id INTEGER PRIMARY KEY AUTOINCREMENT,Codigo_Asesor TEXT, Fecha TEXT,Hora TEXT,Localizacion TEXT,Tipo Text)";
		estructura[17] = "CREATE TABLE IF NOT EXISTS Control_GPS (Id INTEGER PRIMARY KEY AUTOINCREMENT,Codigo_Asesor TEXT, Fecha TEXT,Hora TEXT,Activo TEXT,Tipo TEXT)";
		estructura[18] = "CREATE TABLE IF NOT EXISTS clientes(id_asesoria INTEGER UNIQUE,TipoDocumento TEXT,Cedula TEXT,Expedicion TEXT,LugarExpedicion TEXT,Nombre TEXT,FechaNacimiento TEXT,Telefono TEXT,TelefonoDestino TEXT,Telefono2 TEXT,Celular TEXT,Celular2 TEXT,Estrato TEXT,Barrio TEXT,Direccion TEXT,Paginacion TEXT,Contrato TEXT,PuntoReferencia TEXT,TipoServicio TEXT,Departamento TEXT,Ciudad TEXT,Correo TEXT,Login TEXT,observaciones TEXT,tipoCliente INTEGER,NivelEstudio TEXT,Profesion TEXT,Ocupacion TEXT,Cargo TEXT,NivelIngresos TEXT,EstadoCivil TEXT,Genero TEXT,PersonasCargo INTEGER,TipoVivienda TEXT,TipoPredio TEXT,NombrePredio TEXT)";
		estructura[19] = "CREATE TABLE IF NOT EXISTS barrios(id INTEGER PRIMARY KEY AUTOINCREMENT, ciudad TEXT, barrio TEXT, codigo INTEGER, zona INTEGER, activo INTEGER)";
		estructura[20] = "CREATE TABLE IF NOT EXISTS impuestos(id INTEGER PRIMARY KEY AUTOINCREMENT, departamento TEXT, municipio TEXT, estrato TEXT, valor INTEGER)";
		estructura[21] = "CREATE TABLE IF NOT EXISTS reglas(id INTEGER PRIMARY KEY AUTOINCREMENT, regla TEXT, nuevo TEXT, Tipo_Producto TEXT, Plan TEXT, Descuento TEXT, Meses TEXT, Cantidad TEXT)";
		estructura[22] = "CREATE TABLE IF NOT EXISTS prospecto(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,productos TEXT,motivo TEXT,observaciones TEXT,distancia INTEGER)";
		estructura[23] = "CREATE TABLE IF NOT EXISTS cotizacion(id INTEGER PRIMARY KEY AUTOINCREMENT,id_asesoria INTEGER,telefonia TEXT,toInd TEXT,toEmp TEXT,toDInd TEXT,toDEmp TEXT,promoTo TEXT,television TEXT,tvInd TEXT,tvEmp TEXT,tvDInd TEXT,tvDEmp TEXT,promoTv TEXT,adicionales TEXT,precioAdicionales TEXT,adInd TEXT,adEmp TEXT,internet TEXT,baInd TEXT,baEmp TEXT,baDInd TEXT,baDEmp TEXT,promoBa TEXT,internet3g TEXT,internet3gInd TEXT,internet3gEmp TEXT,internet3gDInd TEXT,internet3gDEmp TEXT,promo3g TEXT,internet4g TEXT,internet4gInd TEXT,internet4gEmp TEXT,internet4gDInd TEXT,internet4gDEmp TEXT,promo4g TEXT,totalInd  TEXT,totalEmp TEXT,contadorProductos TEXT,estrato TEXT,validoIVR TEXT)";
		estructura[24] = "CREATE TABLE IF NOT EXISTS contacto(id INTEGER PRIMARY KEY AUTOINCREMENT,id_cliente TEXT,nombre TEXT,telefono TEXT,celular TEXT,parentesco TEXT)";
		estructura[25] = "CREATE TABLE IF NOT EXISTS facturacion(id INTEGER PRIMARY KEY AUTOINCREMENT,id_cliente INTEGER,Departamento TEXT,Municipio TEXT,Direccion TEXT,Barrio TEXT)";
		estructura[26] = "CREATE TABLE IF NOT EXISTS listasgenerales(id INTEGER PRIMARY KEY AUTOINCREMENT,lst_departamento TEXT, lst_ciudad TEXT, lst_nombre TEXT, lst_item TEXT)";
		estructura[27] = "CREATE TABLE IF NOT EXISTS listasvalores(id INTEGER PRIMARY KEY AUTOINCREMENT,lst_departamento TEXT, lst_ciudad TEXT, lst_nombre TEXT, lst_clave TEXT, lst_valor TEXT)";
		estructura[28] = "CREATE TABLE IF NOT EXISTS listasestratos(id INTEGER PRIMARY KEY AUTOINCREMENT,lst_departamento TEXT, lst_ciudad TEXT, lst_nombre TEXT, lst_estrato INT, lst_item TEXT)";
		estructura[29] = "CREATE TABLE IF NOT EXISTS promocionesmovilidad(id INTEGER PRIMARY KEY AUTOINCREMENT, prm_planid TEXT, prm_nombre TEXT, prm_homologado TEXT)";
		estructura[30] = "CREATE TABLE IF NOT EXISTS gpon(id INTEGER PRIMARY KEY AUTOINCREMENT, gpn_ciudad TEXT, gpn_inicial TEXT, gpn_final TEXT)";
		estructura[31] = "CREATE TABLE IF NOT EXISTS condiciones(id INTEGER, clave TEXT, valor TEXT, tipo TEXT)";
		estructura[32] = "CREATE TABLE IF NOT EXISTS condicionesxreglas(id_regla INTEGER, id_condicion INTEGER, prioridad INTEGER, smartPromo INTEGER)";
		estructura[33] = "CREATE TABLE IF NOT EXISTS decos(id_confdeco INTEGER, caracteristica TEXT, configuracion TEXT)";
		estructura[34] = "CREATE TABLE IF NOT EXISTS condicionesxdecos(id_condicion INTEGER, id_decos INTEGER)";
		estructura[35] = "CREATE TABLE IF NOT EXISTS permisos(rol TEXT, accion TEXT)";
		estructura[36] = "CREATE TABLE IF NOT EXISTS pagoparcialanticipado(producto TEXT, servicio TEXT, pagoparcial DOUBLE, descuento TEXT, pagoanticipado DOUBLE)";
		estructura[37] = "CREATE TABLE IF NOT EXISTS pagoParcial(id INTEGER PRIMARY KEY AUTOINCREMENT, producto TEXT, valor DOUBLE,descuento DOUBLE, tipoPaquete INTEGER, idPaquete INTEGER, idPagoParcialPaquete INTEGER, tipoTransacion INTEGER, tipoHogar TEXT)";
        estructura[38] = "CREATE TABLE IF NOT EXISTS valorconexion(id INTEGER PRIMARY KEY AUTOINCREMENT, productos TEXT, valor DOUBLE)";
		estructura[39] = "CREATE TABLE IF NOT EXISTS condicionesxtarifas(id_tarifa INTEGER,id_condicion INTEGER,activo INTEGER )";

    }

    public boolean insertar(String tabla, ContentValues datos) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        if (db.insert(tabla, null, datos) != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean actualizar(String tabla, ContentValues datos, String clausula, String[] argumentos) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        if (db.update(tabla, datos, clausula, argumentos) >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean eliminar(String tabla, String clausula, String[] argumentos) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        if (db.delete(tabla, clausula, argumentos) >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ArrayList<String>> consultar(boolean distinct, String tabla, String[] columnas, String clausula,
                                                  String[] argumentos, String groupBy, String orderBy, String limit) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
        Cursor result = db.query(distinct, tabla, columnas, clausula, argumentos, groupBy, null, orderBy, limit);
        if (result != null) {
            int cantidad = result.getCount();
            if (cantidad > 0) {
                if (result.moveToFirst()) {
                    do {
                        ArrayList<String> fila = new ArrayList<String>();
                        for (int i = 0; i < result.getColumnCount(); i++) {
                            fila.add(result.getString(i));
                        }
                        resultado.add(fila);
                    } while (result.moveToNext());
                }
            } else {
                resultado = null;
            }
            result.close();
            db.close();
        } else {
            resultado = null;
        }
        return resultado;
    }

    public ArrayList<ArrayList<String>> consultar2(String Query) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
        Cursor result = db.rawQuery(Query, null);
        int cantidad = result.getCount();
        if (cantidad > 0) {
            if (result.moveToFirst()) {
                do {
                    ArrayList<String> fila = new ArrayList<String>();
                    for (int i = 0; i < result.getColumnCount(); i++) {
                        fila.add(result.getString(i));
                    }
                    resultado.add(fila);
                } while (result.moveToNext());
            }
        } else {
            resultado = null;
        }
        result.close();
        db.close();
        return resultado;
    }

    public ArrayList<ArrayList<String>> consultarDescuentos(String Query) {
        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
        Cursor result = db.rawQuery(Query, null);
        int cantidad = result.getCount();
        if (cantidad > 0) {
            if (result.moveToFirst()) {
                do {
                    ArrayList<String> fila = new ArrayList<String>();
                    for (int i = 0; i < result.getColumnCount(); i++) {
                        fila.add(result.getString(i));
                    }
                    resultado.add(fila);
                } while (result.moveToNext());
            }
        } else {
            resultado = null;
        }
        result.close();
        db.close();
        return resultado;
    }

    public ArrayList<ArrayList<String>> consultarAvanzado(String sql, String[] valores) {

        SQLiteDatabase db = MainActivity.basedatos.getWritableDatabase();
        ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
        Cursor result = db.rawQuery(sql, valores);

        int cantidad = result.getCount();
        if (cantidad > 0) {
            if (result.moveToFirst()) {
                do {
                    ArrayList<String> fila = new ArrayList<String>();
                    for (int i = 0; i < result.getColumnCount(); i++) {
                        fila.add(result.getString(i));
                    }
                    resultado.add(fila);
                } while (result.moveToNext());
            }
        } else {
            resultado = null;
        }
        result.close();
        db.close();
        return resultado;
    }

    public ContentValues generarParametros(String parametros) {
        ContentValues cv = new ContentValues();
        return cv;
    }
}
