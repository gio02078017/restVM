package co.com.une.appmovilesune.complements;

/*Importo librerias para crear PDF*/

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Venta;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf {

	private final static String NOMBRE_DIRECTORIO = "AppMovilesUne/Cotizaciones";
	private final static String ETIQUETA_ERROR = "ERROR";

	public void pdf(String NombrePdf, Cliente cliente, Venta venta) {

		String NOMBRE_DOCUMENTO = "Bienvenido.pdf";

		// Creamos el documento.
		Document documento = new Document();
		try {

			// Creamos el fichero con el nombre que deseemos.
			File f = crearFichero(NOMBRE_DOCUMENTO);

			// Creamos el flujo de datos de salida para el fichero donde
			// guardaremos el pdf.
			FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

			// Asociamos el flujo que acabamos de crear al documento.
			PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

			documento.setPageSize(new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()));

			// Abrimos el documento.
			documento.open();

			int imgCabezote = R.drawable.cabezote;
			int imgData = R.drawable.data;
			float heightDeviceHeader = 720;
			float heightDeviceData = 630;
			TelephonyManager manager = (TelephonyManager) MainActivity.context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
				// System.out.println("Tablet");
				imgCabezote = R.drawable.cabezotetablet;
				imgData = R.drawable.datatablet;
				heightDeviceHeader = 685;
				heightDeviceData = 648;
			}

			// Imagen en el Header
			Bitmap bitmapHeader = BitmapFactory.decodeResource(MainActivity.context.getResources(), imgCabezote);
			ByteArrayOutputStream streamHeader = new ByteArrayOutputStream();
			bitmapHeader.compress(Bitmap.CompressFormat.PNG, 100, streamHeader);
			Image headerImg = Image.getInstance(streamHeader.toByteArray());
			headerImg.setAbsolutePosition(10, 30);
			PdfContentByte byteHeader = writer.getDirectContent();
			PdfTemplate tpHeader = byteHeader.createTemplate(600, 150);
			tpHeader.addImage(headerImg);

			byteHeader.addTemplate(tpHeader, -15, heightDeviceHeader);
			Phrase phraseHeader = new Phrase(byteHeader + "",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));

			// Imagen de fechas y numero de solicitud
			Bitmap bitmapData = BitmapFactory.decodeResource(MainActivity.context.getResources(), imgData);
			ByteArrayOutputStream streamData = new ByteArrayOutputStream();
			bitmapData.compress(Bitmap.CompressFormat.PNG, 100, streamData);
			Image DataImg = Image.getInstance(streamData.toByteArray());
			DataImg.setAbsolutePosition(10, 30);
			PdfContentByte byteData = writer.getDirectContent();
			PdfTemplate tpData = byteData.createTemplate(600, 150);
			tpData.addImage(DataImg);
			byteData.addTemplate(tpData, 235, heightDeviceData);
			Phrase phraseData = new Phrase(byteData + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));

			// Creo la fuente
			BaseFont tirana = BaseFont.createFont();

			FontFactory.register("assets/Tirana.otf", "Tirana");
			// Font fontNormalGray =
			// FontFactory.getFont("tirana",11,Font.NORMAL);

			// Creamos estilos para las frases dirigidas al cliente con fecha y
			// nombre de cliente, y tabla
			Font fontNormalGray = FontFactory.getFont("Tirana", 10, Font.NORMAL);
			fontNormalGray.setColor(153, 153, 153);
			Font fontNormalGrayTen = FontFactory.getFont("Tirana", 10, Font.NORMAL);
			fontNormalGrayTen.setColor(153, 153, 153);
			Font fontCursiveGray = FontFactory.getFont("Tirana", 10, Font.ITALIC);
			fontCursiveGray.setColor(153, 153, 153);
			Font fontBoldText = FontFactory.getFont("Tirana", 11, Font.BOLD);
			fontBoldText.setColor(153, 153, 153);
			Font fontNormalRed = FontFactory.getFont("Tirana", 10, Font.NORMAL);
			fontNormalRed.setColor(255, 19, 0);
			Font fontBoldRed = FontFactory.getFont("Tirana", 10, Font.BOLD);
			fontBoldRed.setColor(255, 19, 0);
			Font fontBoldTextPlanPurple = FontFactory.getFont("Tirana", 10, Font.BOLD);
			fontBoldTextPlanPurple.setColor(107, 30, 139);
			Font fontPurpleTitle = FontFactory.getFont("Tirana", 10, Font.BOLD);
			fontPurpleTitle.setColor(107, 30, 139);
			Font fontPurpleNormal = FontFactory.getFont("Tirana", 10, Font.NORMAL);
			fontPurpleNormal.setColor(107, 30, 139);

			// Añadimos un título con una fuente personalizada.
			// documento.add(new Paragraph("Cotizador UNE", font));
			documento.add(new Paragraph(" "));
			documento.add(new Paragraph(" "));
			documento.add(new Paragraph(" "));

			// Añadimos un texto con informacion hacia el cliente.
			String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()); // Creamos
																													// fecha
			Paragraph nameClient = new Paragraph("Sr.(a) " + cliente.getNombre() + "\n", fontNormalGray);
			Paragraph textCliente = new Paragraph(
					"A continuación te especificamos los servicios que acabas de adquirir con nosotros:",
					fontNormalGray);
			Phrase textTitleOrden = new Phrase("Numero de solicitud: ", fontBoldText);
			Phrase textTitleOrdenResult = new Phrase(NombrePdf + " (Venta Móvil)", fontNormalGray);
			Phrase textTitleFecha = new Phrase("Fecha de instalación: ", fontBoldText);
			Phrase textTitleFechaVenta = new Phrase("Fecha de Venta: ", fontBoldText);
			Phrase textPagoTotalNum = new Phrase("$15000", fontNormalGray);
			Phrase textTitleFechaResultVenta = new Phrase(mydate, fontNormalGray);

			/* Texto general de UNE */
			Phrase une = new Phrase("UNE.\n", fontNormalRed);
			Phrase textUnirse = new Phrase(
					"Te acabas de unir a lo mejor, comienza a disfrutar de los beneficios que tienes por ser parte de ",
					fontNormalGray);
			Phrase pointPurple = new Phrase("• ", fontPurpleNormal);
			Phrase textListaServiciosUno = new Phrase("Ingresa a www.une.com.co/unemas, inscríbete a ", fontNormalGray);
			Phrase textListaServiciosUnoa = new Phrase("UNE MÁS", fontPurpleNormal);
			Phrase textListaServiciosUnob = new Phrase(
					", acumula puntos, redímelos por premios de nuestro catálogo, disfruta de los descuentos de nuestros aliados y participa en sorteos de experiencias.\n",
					fontNormalGray);
			Phrase textListaServiciosDos = new Phrase("Inscríbete a ", fontNormalGray);
			Phrase textListaServiciosDosa = new Phrase("Factura Web", fontPurpleNormal);
			Phrase textListaServiciosDosb = new Phrase(
					" para que recibas tu factura en tu correo electrónico y pagues desde Internet en la comodidad de tu casa u oficina. Ingresa a www.une.com.co/facturaweb.\n",
					fontNormalGray);
			Phrase textListaServiciosTres = new Phrase("Centro de Servicios 01 8000 42 22 22 ", fontPurpleNormal);
			Phrase textListaServiciosTresa = new Phrase(
					"para que reportes daños, quejas, consultes acerca de tu factura o sobre los consumos de tu línea telefónica, traslades tus servicios actuales a tu nuevo hogar y cambies o modifiques tus servicios actuales.\n",
					fontNormalGray);
			Phrase textListaServiciosCuatro = new Phrase("Centro de Ventas 01 8000 41 11 11 ", fontPurpleNormal);
			Phrase textListaServiciosCuatroa = new Phrase(
					"para que estés actualizado, adquieras nuevos servicios y disfrutes lo mejor dentro y fuera de tu hogar.\n",
					fontNormalGray);

			/*
			 * Textos de los diferentes productos que se pueden adquirir en UNE
			 */
			Phrase textPlanTv = new Phrase(
					"Televisión Digital UNE con programación para todos, la mayor cantidad de canales HD, canales premium para una gran experiencia en casa.\n",
					fontBoldRed);
			Phrase textPlanBa = new Phrase("Internet banda ancha para navegar a toda velocidad.\n", fontBoldRed);
			Phrase textPlanTo = new Phrase(
					"Telefonía fija con excelente calidad, tarifas y planes para llamar a donde quieras.\n",
					fontBoldRed);
			Phrase textPlanCuatroG = new Phrase("4G LTE - El Internet móvil más rápido de Colombia.\n", fontBoldRed);

			/* Los planes que toman en UNE */
			Phrase observaciones = new Phrase("Observaciones: ", fontBoldRed);
			Phrase nullPlan = new Phrase(
					"________________________________________________________________________________________________",
					fontNormalGray);
			Phrase nullObs = new Phrase(
					"______________________________________________________________________________________________________________________________________________________________________________________________________\n",
					fontCursiveGray);

			Phrase textPlanTvSol = null;
			Phrase textTitleDTv = null;
			Phrase textPlanTvPrice = null;

			Phrase textPlanToSol = null;
			Phrase textTitleDtoTo = null;
			Phrase textPlanToPrice = null;

			Phrase textPlanBaSol = null;
			Phrase textTitleDtoBa = null;
			Phrase textPlanBaPrice = null;

			Phrase textPlanCuatroGSol = null;
			Phrase textTitleDtoCuatroG = null;
			Phrase textPlanCuatroGPrice = null;

			Phrase observacionesSol = null;

			/* Comprobando que datos poner en cada uno de los planes */
			if (!venta.getTelevision()[0].equalsIgnoreCase("-")) {
				textPlanTvSol = new Phrase(venta.getTelevision()[0], fontNormalGray);
				if (!venta.getTelevision()[4].equalsIgnoreCase("N/A")) {
					textTitleDTv = new Phrase(
							"Con un descuento de " + venta.getTelevision()[3] + " durante " + venta.getTelevision()[4],
							fontNormalGray);
				}
				textPlanTvPrice = new Phrase("($" + venta.getTelevision()[1] + " mensuales, IVA incluido)",
						fontCursiveGray);
			} else {
				textPlanTvSol = nullPlan;
			}

			if (!venta.getTelefonia()[0].equalsIgnoreCase("-")) {
				textPlanToSol = new Phrase(venta.getTelefonia()[0], fontNormalGray);
				if (!venta.getTelefonia()[7].equalsIgnoreCase("N/A")) {
					textTitleDtoTo = new Phrase(
							"Con un descuento de " + venta.getTelefonia()[6] + " durante " + venta.getTelefonia()[7],
							fontNormalGray);
				}
				textPlanToPrice = new Phrase("($" + venta.getTelefonia()[1] + " mensuales, IVA incluido)",
						fontCursiveGray);
			} else {
				textPlanToSol = nullPlan;
			}

			if (!venta.getInternet()[0].equalsIgnoreCase("-") && venta.getInternet()[0].toString() != null) {
				textPlanBaSol = new Phrase(venta.getInternet()[0], fontNormalGray);
				if (!venta.getInternet()[6].equalsIgnoreCase("N/A")) {
					textTitleDtoBa = new Phrase(
							"Con un descuento de " + venta.getInternet()[5] + " durante " + venta.getInternet()[6],
							fontNormalGray);
				}
				textPlanBaPrice = new Phrase("($" + venta.getInternet()[1] + " mensuales, IVA incluido)",
						fontCursiveGray);
			} else {
				textPlanBaSol = nullPlan;
			}

			if (!venta.getInternet4G()[0].equalsIgnoreCase("-") && !venta.getTelevision()[6].trim().equals("")) {
				textPlanCuatroGSol = new Phrase(venta.getInternet4G()[0], fontNormalGray);
				if (!venta.getInternet4G()[3].equalsIgnoreCase("N/A")) {
					textTitleDtoCuatroG = new Phrase(
							"Con un descuento de " + venta.getInternet4G()[2] + " durante " + venta.getInternet4G()[3],
							fontNormalGray);
				}
				textPlanCuatroGPrice = new Phrase("($" + venta.getInternet4G()[1] + " mensuales, IVA incluido)",
						fontCursiveGray);
			} else {
				textPlanCuatroGSol = nullPlan;
			}

			if (!venta.getTelevision()[6].equalsIgnoreCase("-") && !venta.getTelevision()[6].trim().equals("")) {
				observacionesSol = new Phrase(
						"Recuerde que tambien ha adquirido con UNE adicionales como " + venta.getTelevision()[6]
								+ " por un total de $" + venta.getTelevision()[8] + "  mensuales, IVA incluido",
						fontCursiveGray);
			} else {
				observacionesSol = nullObs;
			}

			/* Textos de recomendaciones del footer */
			Paragraph recomendacionesFooter = new Paragraph(
					"Al momento de la instalación debes tener estos dispositivos: televisor, computador de escritorio, portátil o tablet, teléfono.",
					fontNormalGrayTen);
			recomendacionesFooter.setAlignment(Paragraph.ALIGN_CENTER);

			/* Textos genericos del footer */
			Phrase telefonia = new Phrase("TELÉFONIA", fontNormalGray);
			Phrase television = new Phrase("TELEVISIÓN", fontNormalGray);
			Phrase cuatrog = new Phrase("4G LTE", fontNormalGray);
			Phrase internet = new Phrase("BANDA ANCHA", fontNormalGray);
			Paragraph estarenUne = new Paragraph(
					"\n\nEstar en UNE es hacer que tu vida dentro y fuera de casa sea cada vez mejor", fontBoldRed);
			estarenUne.setAlignment(Paragraph.ALIGN_CENTER);
			Paragraph textMejorar1 = new Paragraph(
					"Cuando quieras mejorar tus productos, ampliar información o resolver alguna inquietud llama a: ",
					fontPurpleNormal);
			textMejorar1.setAlignment(Paragraph.ALIGN_CENTER);
			Paragraph textMejorarBold = new Paragraph(
					"Centro de servicios hogares: 07 8000 42 22 22 - Centro de ventas Hogares: 01 8000 41 11 11",
					fontPurpleTitle);
			textMejorarBold.setAlignment(Paragraph.ALIGN_CENTER);
			Phrase textTitleFechaResult = new Phrase("No definido\n", fontNormalGray);
			if (venta.getHorarioAtencion() != null) {
				textTitleFechaResult = new Phrase(venta.getHorarioAtencion(), fontNormalGray);
			}
			textCliente.setAlignment(Paragraph.ALIGN_JUSTIFIED);

			/**/
			PdfPTable tablaDatosPpal = new PdfPTable(2);
			EstiloTable(tablaDatosPpal, 2f, 3f, 10);

			Paragraph textFooter = new Paragraph(
					"Si deseas comunicarte con nosotros:\nIngresa a http://www.une.com.co/ y das click en la opción 'Contáctenos'\nLlama a nuestro Centro de Servicios al 01 8000 42 22 22 – Centro de Ventas 01 8000 41 11 11\nLíneas gratuitas a nivel nacional llamando desde cualquier línea fija\nVisita nuestras oficinas de Servicio al Cliente en todo el país, encuentra la ubicación de las oficinas en el reverso de tu factura\nInstalación del servicio(s) sujeta a cobertura y disponibilidad",
					fontPurpleTitle);
			textFooter.setAlignment(Paragraph.ALIGN_CENTER);

			Paragraph textFooterVersion = new Paragraph("FP-127", fontPurpleTitle);
			textFooterVersion.setAlignment(Paragraph.ALIGN_RIGHT);

			/* Numero de pedido o ID de venta y fecha de agendamiento */
			PdfPCell cellNumeroSolicitud = new PdfPCell(textTitleOrden);
			PdfPCell cellNumeroSolicitudData = new PdfPCell(textTitleOrdenResult);
			PdfPCell cellFechaSolicitud = new PdfPCell(textTitleFecha);
			PdfPCell cellFechaVenta = new PdfPCell(textTitleFechaVenta);
			PdfPCell cellFechaSolicitudData = new PdfPCell(textTitleFechaResult);
			PdfPCell cellVentaData = new PdfPCell(textTitleFechaResultVenta);

			/* Les pongo borders cero y agrego a la tabla e imprimo tabla */
			cellVentaData.setBorder(0);
			cellVentaData.setPaddingTop(7);
			cellFechaSolicitudData.setBorder(0);
			cellFechaSolicitudData.setPaddingTop(7);
			cellFechaSolicitud.setBorder(0);
			cellFechaSolicitud.setPadding(2);
			cellFechaVenta.setBorder(0);
			cellFechaVenta.setPadding(2);
			cellNumeroSolicitudData.setBorder(0);
			cellNumeroSolicitud.setPadding(2);
			cellNumeroSolicitud.setBorder(0);
			cellNumeroSolicitud.setPadding(2);
			tablaDatosPpal.addCell(cellNumeroSolicitud);
			tablaDatosPpal.addCell(cellNumeroSolicitudData);
			tablaDatosPpal.addCell(cellFechaVenta);
			tablaDatosPpal.addCell(cellVentaData);
			tablaDatosPpal.addCell(cellFechaSolicitud);
			tablaDatosPpal.addCell(cellFechaSolicitudData);

			// Imprimo Tabla
			documento.add(tablaDatosPpal);

			/* Agrego texto de bienvenida */
			documento.add(new Paragraph(" "));
			documento.add(nameClient);
			documento.add(textUnirse);
			documento.add(une);
			documento.add(pointPurple);
			documento.add(textListaServiciosUno);
			documento.add(textListaServiciosUnoa);
			documento.add(textListaServiciosUnob);
			documento.add(pointPurple);
			documento.add(textListaServiciosDos);
			documento.add(textListaServiciosDosa);
			documento.add(textListaServiciosDosb);
			documento.add(pointPurple);
			documento.add(textListaServiciosTres);
			documento.add(textListaServiciosTresa);
			documento.add(pointPurple);
			documento.add(textListaServiciosCuatro);
			documento.add(textListaServiciosCuatroa);

			/* Los datos de venta */
			PdfPTable tablaDatosVentas = new PdfPTable(2);
			tablaDatosVentas.setWidthPercentage(100);

			PdfPCell cellOne = new PdfPCell();
			cellOne.addElement(textPlanTv);
			cellOne.addElement(textPlanTvSol);
			cellOne.addElement(textTitleDTv);
			cellOne.addElement(textPlanTvPrice);
			cellOne.setBorder(0);
			cellOne.setPaddingBottom(5);
			cellOne.setPaddingLeft(2);
			cellOne.setPaddingRight(5);
			PdfPCell cellTwo = new PdfPCell();
			cellTwo.addElement(textPlanBa);
			cellTwo.addElement(textPlanBaSol);
			cellTwo.addElement(textTitleDtoBa);
			cellTwo.addElement(textPlanBaPrice);
			cellTwo.setBorder(0);
			cellTwo.setPaddingBottom(5);
			cellTwo.setPaddingLeft(5);
			cellTwo.setPaddingRight(2);
			PdfPCell cellThree = new PdfPCell();
			cellThree.addElement(textPlanTo);
			cellThree.addElement(textPlanToSol);
			cellThree.addElement(textTitleDtoTo);
			cellThree.addElement(textPlanToPrice);
			cellThree.setBorder(0);
			cellThree.setPaddingBottom(5);
			cellThree.setPaddingLeft(2);
			cellThree.setPaddingRight(5);
			PdfPCell cellFour = new PdfPCell();
			cellFour.addElement(textPlanCuatroG);
			cellFour.addElement(textPlanCuatroGSol);
			cellFour.addElement(textTitleDtoCuatroG);
			cellFour.addElement(textPlanCuatroGPrice);
			cellFour.setBorder(0);
			cellFour.setPaddingBottom(5);
			cellFour.setPaddingLeft(2);
			cellFour.setPaddingRight(5);

			tablaDatosVentas.addCell(cellOne);
			tablaDatosVentas.addCell(cellTwo);
			tablaDatosVentas.addCell(cellThree);
			tablaDatosVentas.addCell(cellFour);

			documento.add(tablaDatosVentas);
			documento.add(observaciones);
			documento.add(observacionesSol);
			documento.add(new Paragraph(" "));
			documento.add(recomendacionesFooter);

			PdfPTable tablaFooter = new PdfPTable(5);
			PdfPCell cellOneFooter = new PdfPCell(estarenUne);
			cellOneFooter.setBorder(0);
			cellOneFooter.setHorizontalAlignment(Element.ALIGN_CENTER);

			tableInfoFooter(R.drawable.pdf_telefonia, telefonia, tablaFooter);
			tableInfoFooter(R.drawable.pdf_ba, internet, tablaFooter);
			tablaFooter.addCell(cellOneFooter);
			tableInfoFooter(R.drawable.pdf_television, television, tablaFooter);
			tableInfoFooter(R.drawable.pdf_cuatrog, cuatrog, tablaFooter);

			documento.add(tablaFooter);
			documento.add(textFooter);
			documento.add(textFooterVersion);

		} catch (DocumentException e) {

			Log.e(ETIQUETA_ERROR, e.getMessage());

		} catch (IOException e) {

			Log.e(ETIQUETA_ERROR, e.getMessage());

		} finally {
			// Cerramos el documento.
			documento.close();
		}
		// sendEmail(cliente, NOMBRE_DOCUMENTO);
	}

	/*
	 * Metodo para enviar el mensaje por correo con el PDF adjunto.
	 */
	public void sendEmail(Cliente cliente, String Pdf) {
		File file = new File(Environment.getExternalStorageDirectory() + "/data/AppMovilesUne/Cotizaciones/", Pdf);
		try {
			/* Login para enviar el correo = Usuario, contraseña */
			GMailSender sender = new GMailSender("solicitudesune@une.net.co", "9000923859");
			/* Envio de correo */
			sender.sendMail(
					/* Asunto */
					"Hola " + cliente.getNombre()
							+ ", ¡Su cotizacion en VentaMovil de Une esta en su bandeja de entrada!",
					/* Cuerpo del mensaje */
					cliente.getNombre()
							+ "\n\nA continuacion verás un adjunto, en donde encontrarás un detalle de tu cotizacion.\nGracias por confiar en nosotros\n\n\nEste es un mensaje generado automaticamente.",
					/* Emisor */
					"Cotizador VentaMovil<solicitudesune@une.net.co>",
					/* Receptor */
					cliente.getCorreo() + ",ventamovilune@gmail.com",
					/* Archivo adjunto que se enviará */
					file);
		} catch (Exception e) {
			Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG);
			Log.e("SendMail", e.getMessage(), e);
		}
	}

	public void sendEmail2() {
		// File file = new File(Environment.getExternalStorageDirectory()+
		// "/data/AppMovilesUne/Cotizaciones/", Pdf);
		try {
			/* Login para enviar el correo = Usuario, contraseña */
			GMailSender sender = new GMailSender("uneventas@une.net.co", "9000923859");
			/* Envio de correo */
			sender.sendMail(
					/* Asunto */
					"Hola " + "Jorge" + ", ¡Su cotizacion en VentaMovil de Une esta en su bandeja de entrada!",
					/* Cuerpo del mensaje */
					"jorge" + "\n\nA continuacion verás un adjunto, en donde encontrarás un detalle de tu cotizacion.\nGracias por confiar en nosotros\n\n\nEste es un mensaje generado automaticamente.",
					/* Emisor */
					"uneventas@une.net.co",
					/* Receptor */
					"jorgealbertoarroyavemanco@gmail.com" + ",ventamovilune@gmail.com",
					/* Archivo adjunto que se enviará */
					null);
		} catch (Exception e) {
			Toast.makeText(MainActivity.context, e.getMessage(), Toast.LENGTH_LONG);
			Log.e("SendMail", e.getMessage(), e);
		}
	}

	/*
	 * Metodo que estiliza la tabla Recibe como parametros una tabla, la cual es
	 * "estilizada"
	 */
	public void EstiloTable(PdfPTable tabla, float one, float two, float padding) {
		tabla.setWidthPercentage(90); // Ponemos porcentaje
		tabla.setSpacingBefore(10f); // Un espacio a la izquierda de la tabla
		tabla.getDefaultCell().setBorder(0); // Le asignamos un borden de cero
		tabla.getDefaultCell().setPadding(padding); // Asignamos un espacio al
													// rededor para que la tabla
													// no se vea apretada
		tabla.getDefaultCell().setHorizontalAlignment(Phrase.ALIGN_LEFT);
		float[] columnWidths = { one, two }; // Para asignar el tamaño de cada
												// celda
		try {
			tabla.setWidths(columnWidths); // Asignacion del tamaño de la celda
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo para llenar la informacion del paquete comprado
	 */
	public void tableInfoFooter(int img, Phrase plan, PdfPTable tabla) {
		// Obtengo el icono de telefonia para imprimir en la celda
		Bitmap bitmapImage = BitmapFactory.decodeResource(MainActivity.context.getResources(), img);
		ByteArrayOutputStream streamImage = new ByteArrayOutputStream();
		bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, streamImage);

		Image imagenImg = null;
		try {
			// Creo la imagen para el PDF
			imagenImg = Image.getInstance(streamImage.toByteArray());
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Asigno variables de imagenes, plan, precio y descuento a las celdas
		PdfPCell imgCell = new PdfPCell();
		imgCell.addElement(imagenImg);
		imgCell.addElement(plan);

		imgCell.setBorder(0);
		imgCell.setPadding(5);
		imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		// Agrego a la tabla la informacion
		tabla.addCell(imgCell);
	}

	/*
	 * Metodo que crea el fichero y devuelve una ruta donde imprimirlo
	 */
	public static File crearFichero(String nombreFichero) throws IOException {
		File ruta = getRuta(); // Obtiene la ruta donde se va a imprimir el
								// fichero
		File fichero = null;
		if (ruta != null)
			fichero = new File(ruta, nombreFichero);
		return fichero;
	}

	/**
	 * Obtenemos la ruta donde vamos a almacenar el fichero.
	 * 
	 * @return
	 */
	public static File getRuta() {

		// El fichero será almacenado en un directorio dentro del directorio
		// Descargas
		File ruta = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			ruta = new File(Environment.getExternalStorageDirectory() + "/data/", NOMBRE_DIRECTORIO); // Se
																										// crea
																										// en
																										// Storage/data/AppMovilesUne/Cotizaciones
			if (ruta != null) {
				if (!ruta.mkdirs()) {
					if (!ruta.exists()) {
						return null;
					}
				}
			}
		}
		return ruta;
	}
}
