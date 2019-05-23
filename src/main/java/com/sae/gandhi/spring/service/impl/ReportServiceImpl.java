package com.sae.gandhi.spring.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sae.gandhi.spring.dao.AlumnoPagoBitacoraDAO;
import com.sae.gandhi.spring.entity.vo.AlumnoPagoBitacoraVO;
import com.sae.gandhi.spring.service.ReportService;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.vo.AlumnoReportVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CursosVO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private AlumnoPagoBitacoraDAO alumnoPagoBitacoraDAO;

	@Override
	public InputStream generateReport(AlumnosVO alumnoVO, CursosVO cursosVO, Integer alumnoId, Integer cursoId) {
		Map<String, Object> mapParams = new HashMap<String, Object>();
		String grado = "";
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
		List<AlumnoPagoBitacoraVO> lstAlumnoPagos;
		List<AlumnoReportVO> lstReporte = new ArrayList<>();
		AlumnoReportVO reportVO;
		
		if(cursosVO!=null)
			grado = "Curso: "+cursosVO.getCursoNombre(); 
			
		mapParams.put("alumno", alumnoVO.getAlumnoNombre()+" "+alumnoVO.getAlumnoApPaterno()+" "+alumnoVO.getAlumnoApMaterno());
		mapParams.put("grado", grado);
		
		if(cursoId == null)
			lstAlumnoPagos = alumnoPagoBitacoraDAO.findByAlumnoId(alumnoId);
		else
			lstAlumnoPagos = alumnoPagoBitacoraDAO.findByAlumnoIdAndCursoId(alumnoId, cursoId);
		
		for (AlumnoPagoBitacoraVO pagos : lstAlumnoPagos) {
			reportVO = new AlumnoReportVO();
	
			reportVO.setfCurso(pagos.getCurso());
			reportVO.setfConcepto(pagos.getConcepto()+ " " +
					SaeDateUtils.formatMonthYear(pagos.getFechaLimite()));
			reportVO.setfMonto(pagos.getMonto()!=null?pagos.getMonto().doubleValue():BigDecimal.ZERO.doubleValue());
			reportVO.setfPago(pagos.getPago()!=null?pagos.getPago().doubleValue():BigDecimal.ZERO.doubleValue());
			reportVO.setfSaldo(pagos.getSaldo()!=null?pagos.getSaldo().doubleValue():BigDecimal.ZERO.doubleValue());
			reportVO.setfFechaPago(pagos.getFechaPago()==null?"":sdf2.format(pagos.getFechaPago()));
			reportVO.setfEstatus(SaeEnums.Pago.getPago(pagos.getEstatus()).getStatus());
			reportVO.setfAdeudo(reportVO.getfMonto()-reportVO.getfPago()-reportVO.getfSaldo());
			
			lstReporte.add(reportVO);
		}
		
		///////////////////////////////////////////////////////////////////
		try{
			JasperReport reporte;
			
			//para debug
			try{
				reporte = (JasperReport) JRLoader.loadObjectFromFile("C:/x/Workspaces/SAE/sae/src/main/resources/reports/reportAlumno.jasper");
			}catch(Exception e){ 
				reporte = (JasperReport) JRLoader.loadObject(ReportServiceImpl.class.getResourceAsStream("/reports/reportAlumno.jasper"));
			}
			//reporte = (JasperReport) JRLoader.loadObjectFromFile("src/main/resources/reports/reportAlumno1.jasper");

			JRBeanCollectionDataSource ds =new JRBeanCollectionDataSource(lstReporte);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, mapParams, ds);
			
			// OutputStream
			final ByteArrayOutputStream report = new ByteArrayOutputStream();

			// PDF
			//JasperExportManager.exportReportToPdfStream(jasperPrint, report);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(report));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			//byte[] reporteByte = report.toByteArray();
			//final InputStream is = new ByteArrayInputStream(reporteByte);
			
			return new ByteArrayInputStream(report.toByteArray());
		}catch (JRException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}

}
