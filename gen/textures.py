from PIL import Image, ImageDraw
import os
A = "src/main/resources/assets/mindclinic/textures"
os.makedirs(A+"/block", exist_ok=True); os.makedirs(A+"/item", exist_ok=True); os.makedirs(A+"/mob_effect", exist_ok=True)
OUT=(60,52,75,255); CREAM=(248,240,221,255); MINT=(176,228,208,255); MINT2=(140,205,182,255)
BLUE=(86,132,214,255); YEL=(240,205,90,255); PINK=(240,150,170,255); PINK2=(214,98,128,255)
LAV=(200,186,236,255); LAV2=(158,140,206,255); TEAL=(40,150,160,255); WHITE=(255,255,255,255)

def border(d,n,c=OUT,w=2):
    for i in range(w): d.rectangle([i,i,n-1-i,n-1-i],outline=c)

def capsule(d,x0,y0,x1,y1,c1,c2):
    # diagonal capsule drawn pixel by pixel
    import math
    dx,dy=x1-x0,y1-y0; L=math.hypot(dx,dy)
    for t in range(int(L)*2+1):
        x=x0+dx*t/(L*2); y=y0+dy*t/(L*2)
        col=c1 if t<L else c2
        for ox in (-2,-1,0,1,2):
            for oy in (-2,-1,0,1,2):
                if ox*ox+oy*oy<=5: d.point((round(x+ox),round(y+oy)),fill=col)

# --- wall
im=Image.new("RGBA",(32,32),MINT); d=ImageDraw.Draw(im)
for p in range(0,32,8):
    d.line([(p,0),(p,31)],fill=MINT2); d.line([(0,p),(31,p)],fill=MINT2)
capsule(d,11,21,21,11,PINK2,WHITE); border(d,32); im.save(A+"/block/clinic_wall.png")
# --- stripe
im=Image.new("RGBA",(32,32),CREAM); d=ImageDraw.Draw(im)
for x in range(0,32,8): d.rectangle([x,0,x+3,25],fill=BLUE)
d.rectangle([0,26,31,31],fill=YEL); d.line([(0,25),(31,25)],fill=OUT); border(d,32); im.save(A+"/block/clinic_stripe.png")
# --- lamp
im=Image.new("RGBA",(32,32),(40,100,115,255)); d=ImageDraw.Draw(im)
d.rectangle([4,4,27,27],fill=TEAL); d.rectangle([8,8,23,23],fill=(110,215,205,255)); d.rectangle([12,12,19,19],fill=(255,250,210,255))
border(d,32,OUT,2); im.save(A+"/block/clinic_lamp.png")
# --- panel with brain
im=Image.new("RGBA",(32,32),LAV); d=ImageDraw.Draw(im)
d.rectangle([4,6,27,25],fill=PINK); 
d.ellipse([4,5,16,26],fill=PINK); d.ellipse([15,5,27,26],fill=PINK)
d.line([(15,6),(15,25)],fill=PINK2); d.line([(16,6),(16,25)],fill=PINK2)
for (a,b,c,e) in [(7,10,12,10),(8,14,13,14),(7,19,12,19),(19,10,24,10),(18,14,23,14),(19,19,24,19)]:
    d.line([(a,b),(c,e)],fill=PINK2)
d.rectangle([13,26,18,28],fill=LAV2)
border(d,32); 
for cx,cy in [(2,2),(28,2),(2,28),(28,28)]: d.rectangle([cx,cy,cx+1,cy+1],fill=LAV2)
im.save(A+"/block/clinic_panel.png")
# --- pill item 16x16
im=Image.new("RGBA",(16,16),(0,0,0,0)); d=ImageDraw.Draw(im)
capsule(d,4,11,11,4,PINK2,WHITE)
# outline
px=im.load(); o=Image.new("RGBA",(16,16),(0,0,0,0)); op=o.load()
for x in range(16):
    for y in range(16):
        if px[x,y][3]==0:
            for dx,dy in((1,0),(-1,0),(0,1),(0,-1)):
                nx,ny=x+dx,y+dy
                if 0<=nx<16 and 0<=ny<16 and px[nx,ny][3]>0: op[x,y]=OUT
for x in range(16):
    for y in range(16):
        if op[x,y][3]>0: px[x,y]=op[x,y]
im.save(A+"/item/pill.png")

# --- effect icons 18x18
def icon(name,bg,draw):
    im=Image.new("RGBA",(18,18),(0,0,0,0)); d=ImageDraw.Draw(im)
    d.rounded_rectangle([0,0,17,17],radius=3,fill=bg,outline=OUT); draw(d); im.save(f"{A}/mob_effect/{name}.png")
def depression(d):
    d.ellipse([3,4,9,9],fill=(200,205,225,255)); d.ellipse([7,3,14,9],fill=(200,205,225,255)); d.rectangle([4,7,13,9],fill=(200,205,225,255))
    for x in (5,8,11): d.line([(x,11),(x-1,14)],fill=(120,170,255,255))
def adhd(d):
    d.polygon([(10,2),(4,10),(8,10),(7,16),(14,7),(9,7)],fill=YEL,outline=OUT)
def schizo(d):
    d.ellipse([2,5,15,12],fill=WHITE,outline=OUT); d.ellipse([6,5,11,12],fill=(120,60,160,255)); d.rectangle([8,7,9,10],fill=OUT)
    d.line([(3,14),(14,3)],fill=(255,70,100,255))
def anxiety(d):
    pts=[(2,9),(4,5),(6,13),(8,5),(10,13),(12,5),(14,13),(16,9)]; d.line(pts,fill=WHITE,width=1)
def insomnia(d):
    d.ellipse([3,3,13,13],fill=(250,235,150,255)); d.ellipse([6,2,15,11],fill=(70,60,120,255))
    d.line([(10,13),(14,13),(10,16),(14,16)],fill=WHITE)
def ocd(d):
    for i,(x,y) in enumerate([(3,3),(10,3),(3,10),(10,10)]):
        d.rectangle([x,y,x+4,y+4],outline=WHITE)
        if i!=3: d.line([(x+1,y+2),(x+2,y+3),(x+4,y)],fill=(120,255,150,255))
def bipolar(d):
    d.pieslice([2,2,15,15],90,270,fill=YEL); d.pieslice([2,2,15,15],270,90,fill=(90,100,200,255))
def autism(d):
    cols=[(255,90,90,255),(255,200,60,255),(90,200,110,255),(90,150,255,255)]
    k=0
    for x in (3,9):
        for y in (3,9):
            d.rectangle([x,y,x+4,y+4],fill=cols[k],outline=OUT); k+=1
icon("depression",(70,80,120,255),depression); icon("adhd",(220,110,60,255),adhd)
icon("schizophrenia",(120,50,110,255),schizo); icon("anxiety",(70,140,150,255),anxiety)
icon("insomnia",(45,45,100,255),insomnia); icon("ocd",(60,110,90,255),ocd)
icon("bipolar",(150,90,160,255),bipolar); icon("autism",(70,70,90,255),autism)
