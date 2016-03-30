package org.jopenexchg;

import java.util.Random;

import org.jopenexchg.hldg.*;
import org.jopenexchg.matcher.*;
import org.jopenexchg.matcher.event.impl.EventHandlerDebugImpl;
import org.jopenexchg.matcher.event.impl.EventHandlerHldgImpl;


public class MatcherDemo
{
	static final int SEED = 20130426;
	static final Random rand = new Random(SEED);
	
	static final int ORDER_MAX = 5000 * 1000;
	static final int ORDER_CNT = 3000 * 1000;
	static final int PRCLD_CNT = 1000;
	static final int STOCK_CNT = 10;
	
	public final static void fakeOrder(Order order)
	{
		int randInt = 0; 
		
		order.pbu = (short)rand.nextInt(3000);
		order.reff = rand.nextInt(999999999);
			
		order.acctType ='A';
		order.accNo = rand.nextInt(999999999);

		randInt = rand.nextInt(2);
		if(randInt == 0)
		{
			order.isbuy = true;
		}
		else
		{
			order.isbuy = false;
		}
		
		order.stockid = 600000 +  rand.nextInt(STOCK_CNT);
		order.ordQty = 	rand.nextInt(1000) + 1;
		order.remQty = order.ordQty;
		order.ordPrc = rand.nextInt(PRCLD_CNT) + 1;
	}
	
	/**
	 *  �Ա����Ե�˵��
	 *  	���ǵ�δ�������յ��߳�Ӧ���ǰ�ȫ���յ�
	 *  	���µ�ȫ��ָ������ɸ�ʽ���������һ
	 *  	Ԥ�ȿ��õĴ����������
	 *  
	 *  	Ԥ�����߳�Ӧ����ǰ���ɨ��������飬����
	 *  	��������֮��д���������
	 *  
	 *  	�����Ĵ���߳�Ӧ����һ����Ԥ����������
	 *  	ҪӦ���ǳֲ���ؼ�顪����Ϊ���Ԥ�����߳�
	 *  	Ҳ��������Ҫ���������Ͳ������ˣ�����߳�
	 *  	��ȻҲӦ��ͬʱ�����
	 *  
	 *  	������������̡߳�Ԥ�����̺߳ʹ���߳��ǲ�
	 *  	��ִ�еģ����Ա����Բ��ԵĴ���̵߳����ܣ�
	 *  	Ϊ�˿۳����������������д���ݵ�ʱ��
	 *  
	 */
	static void speedTest_01()
	{
		try
		{
			int i = 0;
			int stockId;
			Order order = null;
			long prepStart = System.currentTimeMillis();
			Order[] inputQueue = new Order[ORDER_CNT];
			
			Matcher matcher = new Matcher(PRCLD_CNT * STOCK_CNT * 3, ORDER_CNT + 100);

			HldgTable hldgTbl = new HldgTable(ORDER_MAX);
			EventHandlerHldgImpl hndler = new EventHandlerHldgImpl(hldgTbl);
			matcher.setEvtCbs(hndler);

			long prepEnd = System.currentTimeMillis();
			System.out.println("Memory prepared in " + (prepEnd - prepStart + 1) + " ms");
			
			prepStart = System.currentTimeMillis();
			// prepare stocks
			String stockName = null;
			for(i = 0; i < STOCK_CNT; i++)
			{
				stockId = 600000 + i;
				stockName = "_" + stockId + "_";
				
				matcher.addStock(stockId, stockName);
			}
			prepEnd = System.currentTimeMillis();
			System.out.println("Stock prepared in " + (prepEnd - prepStart + 1) + " ms");

			prepStart = System.currentTimeMillis();
			for(i = 0; i < ORDER_CNT; i++)
			{
				order = matcher.allocOrder();
				if(order == null)
				{
					System.out.println("No free order left");
					break;					
				}
				
				fakeOrder(order);
				inputQueue[i] = order;
			}

			prepEnd = System.currentTimeMillis();
			System.out.println("Order list prepared in " + (prepEnd - prepStart + 1) + " ms");
			
			// generate enough orders
			long start = System.currentTimeMillis();
			
			for(i = 0; i < ORDER_CNT; i++)
			{
				// if(false == matcher.ocallInsOrder(ordr))
				if(false == matcher.matchInsOrder(inputQueue[i]))
				{
					System.out.println("Error occured");
				}
			}
			long end = System.currentTimeMillis();
			
			System.out.println(ORDER_CNT + " orders matched and inserted with hldgUpd in " + (end - start + 1) + " ms");
			System.out.println("Speed is " + ORDER_CNT / ((double)(end - start + 1) / 1000.0) + " orders/s");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	static void funcTest_02()
	{
		try
		{
			Matcher matcher = new Matcher(200, 1000);
			EventHandlerDebugImpl evtCbs = new EventHandlerDebugImpl();
			
			matcher.setEvtCbs(evtCbs);
			
			int stockId = 600688;
			String stockName = "_" + stockId + "_";
			TradedInst stock = matcher.addStock(stockId, stockName);

			Order ordr = null;
			
			// Buy eating Sell
			// Sell Side 
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 10;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 40;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);			
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 100;
			ordr.ordPrc = 1100;
			matcher.ocallInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 30;
			ordr.ordPrc = 1000;
			matcher.ocallInsOrder(ordr);	
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 70;
			ordr.ordPrc = 1000;
			matcher.ocallInsOrder(ordr);	
			
			// Buy Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 25;
			ordr.ordPrc = 1300;
			matcher.ocallInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 25;
			ordr.ordPrc = 1300;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 30;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 20;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);			
			
			CallAuctionResult result = new CallAuctionResult();
			boolean bOK = matcher.calcCallAuction(stock, result);
			
			System.out.println("**************");
			bOK = matcher.doCallAuction(stock, result);
			
			System.out.println("");
			System.out.println("doOCall bOK = " + bOK + " ; price = " + result.price + " ; qty = " + result.volume);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
		

	static void funcTest_01()
	{
		try
		{
			Matcher matcher = new Matcher(200, 1000);
			
			EventHandlerDebugImpl evtCb = new EventHandlerDebugImpl();
			matcher.setEvtCbs(evtCb);
			
			int stockId = 600688;
			String stockName = "_" + stockId + "_";
			matcher.addStock(stockId, stockName);

			stockId = 600666;
			stockName = "_" + stockId + "_";
			matcher.addStock(stockId, stockName);
			
			Order ordr = null;
			
			// Buy eating Sell
			// Sell Side 
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 100;
			ordr.ordPrc = 512;
			matcher.matchInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 200;
			ordr.ordPrc = 512;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 150;
			ordr.ordPrc = 515;
			matcher.matchInsOrder(ordr);	

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 305;
			ordr.ordPrc = 515;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 400;
			ordr.ordPrc = 518;
			matcher.matchInsOrder(ordr);
			
			// Incoming Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 1000;
			ordr.ordPrc = 518;
			matcher.matchInsOrder(ordr);
			
			// Sell eating Buy
			// Buy Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 100;
			ordr.ordPrc = 1050;
			matcher.matchInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 200;
			ordr.ordPrc = 1050;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 150;
			ordr.ordPrc = 1047;
			matcher.matchInsOrder(ordr);	

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 305;
			ordr.ordPrc = 1047;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 400;
			ordr.ordPrc = 1045;
			matcher.matchInsOrder(ordr);
			
			// Incoming Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = false;
			ordr.ordQty = 1000;
			ordr.ordPrc = 1044;
			matcher.matchInsOrder(ordr);			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
			
	static void randTest()
	{
		int i = 0;
		Order ordr = new Order();

		// generate enough orders
		long start = System.currentTimeMillis();
		
		for(i = 0; i < ORDER_CNT; i++)
		{
			fakeOrder(ordr);
		}
		long end = System.currentTimeMillis();
		
		System.out.println(ORDER_CNT + " orders faked in " + (end - start + 1) + " ms");
		System.out.println("Speed is " + ORDER_CNT / ((double)(end - start + 1) / 1000.0) + " orders/s");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//funcTest_01();
		speedTest_01();
		// funcTest_02();
		// randTest();   // 610ms
	}

}
