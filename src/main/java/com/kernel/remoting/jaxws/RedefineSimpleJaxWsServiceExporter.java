//package com.kernel.remoting.jaxws;
//
//import javax.xml.ws.Endpoint;
//
//import org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter;
//
//public class RedefineSimpleJaxWsServiceExporter extends SimpleJaxWsServiceExporter {
//
//	private IRedefineBaseAddress redefineBaseAddress;
//
//	public void setRedefineBaseAddress(IRedefineBaseAddress redefineBaseAddress) {
//		this.redefineBaseAddress = redefineBaseAddress;
//	}
//
//	@Override
//	protected String calculateEndpointAddress(Endpoint endpoint,
//			String serviceName) {
//
//		Object implementor = endpoint.getImplementor();
//		if(null != implementor){
//
//			Class<?> cls = endpoint.getImplementor().getClass();
//			RedefineWsIndicator indicator = cls.getAnnotation(RedefineWsIndicator.class);
//			if(null != indicator){
//				return redefineBaseAddress.getBaseAddress(indicator.value()) + serviceName;
//			}
//
//		}
//
//		return super.calculateEndpointAddress(endpoint, serviceName);
//	}
//
//}
