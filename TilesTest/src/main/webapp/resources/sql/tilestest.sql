create table tilestest_img_advertise
(imgno          number not null
,imgfilename    varchar2(100) not null
,constraint PK_tilestest_img_advertise primary key(imgno)
);

create sequence seq_img_advertise
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tilestest_img_advertise values(seq_img_advertise.nextval, '미샤.png');
insert into tilestest_img_advertise values(seq_img_advertise.nextval, '원더플레이스.png');
insert into tilestest_img_advertise values(seq_img_advertise.nextval, '레노보.png');
insert into tilestest_img_advertise values(seq_img_advertise.nextval, '동원.png');
commit;

select *
from tilestest_img_advertise
order by imgno desc;


create table tilestest_product
(productno       number not null          -- 제품번호(대분류, 시퀀스)
,productname     varchar2(100) not null   -- 제품명
,constraint PK_tilestest_product primary key(productno)
);

create sequence seq_tilestest_product
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tilestest_product values(seq_tilestest_product.nextval, '감자깡');
insert into tilestest_product values(seq_tilestest_product.nextval, '새우깡');
insert into tilestest_product values(seq_tilestest_product.nextval, '양파링');
insert into tilestest_product values(seq_tilestest_product.nextval, '고구마깡');
insert into tilestest_product values(seq_tilestest_product.nextval, '빼빼로');
commit;

select *
from tilestest_product;

create table tilestest_productDetail
(productDetailno        number not null         -- 제품상세번호(소분류, 시퀀스)
,fk_productno           number not null         -- 제품번호(대분류)
,productDetailTypeNo    number not null         -- 제품상세타입번호(종류번호)
,productDetailName      varchar2(100) not null  -- 제품상세타입이름(종류명)
,constraint PK_tilestest_productDetail primary key(productDetailno)
,constraint FK_tilestest_productDetail foreign key(fk_productno)
                                     references tilestest_product(productno)
);


create sequence seq_tilestest_productDetail
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 1, 1, '매운맛');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 1, 2, '순한맛');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 1, 3, '달콤맛');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 2, 1, '노래방용');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 2, 2, '대용량');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 2, 3, '중량');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 2, 4, '소량');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 3, 1, '고소한맛');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 3, 2, '순한맛');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 4, 1, '국산');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 4, 2, '중국산');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 5, 1, '롯데');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 5, 2, '오리온');
insert into tilestest_productDetail values(seq_tilestest_productDetail.nextval, 5, 3, '해태');

commit;

select *
from tilestest_productDetail;

select productdetailtypeno, productdetailname
from tilestest_productDetail
where fk_productno = ( select productno
                       from tilestest_product
                       where productname = '새우깡');


create table tilestest_order
(orderno     number not null
,memberid    varchar2(30) default 'yoonchanyoung' not null
,orderday    date default sysdate
,constraint  PK_tilestest_order primary key(orderno)
);


create sequence seq_tilestest_order
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tilestest_order(orderno, memberid, orderday) 
values(seq_tilestest_order.nextval, 'yoonchanyoung', default);

commit;

select *
from tilestest_order;

-- drop table tilestest_jumunDetail purge;
create table tilestest_orderDetail
(orderDetailno      number not null   -- 주문상세번호(시퀀스)
,fk_orderno         number not null   -- 주문번호(전표)
,fk_productDetailno   number not null   -- 제품상세번호
,qty                number not null   -- 주문량
,constraint  PK_orderDetail primary key(orderDetailno)
,constraint  FK_orderDetail_orderno foreign key(fk_orderno)
                                               references tilestest_order(orderno)
,constraint  FK_orderDetail_productDetailno foreign key(fk_productDetailno)     
                                               references tilestest_productDetail(productDetailno)
);

create sequence seq_tilestest_orderDetail
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tilestest_orderDetail(orderDetailno, fk_orderno, fk_productDetailno, qty)
values(seq_tilestest_orderDetail.nextval, (select max(orderno) from tilestest_order), 1, 3);


insert into tilestest_orderDetail(orderDetailno, fk_orderno, fk_productDetailno, qty)
values(seq_tilestest_orderDetail.nextval, 8, 1, 3);



select *
from tilestest_orderDetail;


select productno
from tilestest_product
where productname = '새우깡';

select *
from tilestest_productDetail
where fk_productno = 1;

select rank() over(order by sum(A.qty) desc) as rank,
       C.productname, sum(A.qty) as totalqty,
       trunc( sum(A.qty)/(select sum(qty) from tilestest_orderDetail) * 100, 1) as percent
from tilestest_orderDetail A left join tilestest_productDetail B
on A.fk_productDetailno = B.productDetailno
left join tilestest_product C
on B.fk_productno = C.productno
group by c.productname;


select B.productdetailname, 
       trunc( sum(A.qty)/(select sum(qty) from tilestest_orderDetail) * 100, 1) as percent
from tilestest_orderDetail A left join tilestest_productDetail B
on A.fk_productDetailno = B.productDetailno
left join tilestest_product C
on B.fk_productno = C.productno
where C.productname = '빼빼로'
group by B.productdetailname;


/*
  select rank, jepumname, jepumdetailname, totalqty, percent
  from
  (
  select rank() over(order by sum(A.qty) desc) as rank,
         C.jepumname, B.jepumdetailname, sum(A.qty) as totalqty,
         trunc( sum(A.qty)/(select sum(qty) from tilestest_jumunDetail) * 100, 1) as percent
  from tilestest_jumunDetail A left join tilestest_jepumDetail B
  on A.fk_jepumDetailno = B.jepumDetailno
  left join tilestest_jepum C
  on B.fk_jepumno = C.jepumno
  group by c.jepumname, b.jepumdetailname
  ) V
  where V.jepumname = '감자깡';
*/
---------------------------------------------------------------------------------------------



