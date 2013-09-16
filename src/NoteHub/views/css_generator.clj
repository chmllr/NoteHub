(ns NoteHub.views.css-generator
  (:use 
    [cssgen]
    [cssgen.types :only [px %]]
    [NoteHub.settings]))

(defn- gen-fontlist [& fonts] 
  (apply str 
         (interpose "," 
                    (map #(str "'" % "'") 
                         (filter identity fonts)))))

; CSS Mixins
(def page-width
  (get-setting :page-width #(Integer/parseInt %) 800))

(def helvetica-neue
  (mixin
    :font-weight 300
    :font-family (gen-fontlist "Helvetica Neue"
                               "Helvetica"
                               "Arial"
                               "Lucida Grande"
                               "sans-serif")))

(def central-element
  (mixin
    :width (px page-width)
    :margin-left "auto"
    :margin-right "auto"))

(defn thin-border [foreground]
  (mixin :border-radius :3px
         :border [:1px :solid foreground]))

; Resolves the theme name & tone parameter to a concrete color
(defn- color [& keys]
  (get-in {:dark {:background :#333
                  :foreground :#ccc
                  :background-halftone :#444
                  :foreground-halftone :#bbb
                  :link {:fresh :#6b8
                         :visited :#496
                         :hover :#7c9 }}
           :solarized {:background :#073642
                       :foreground :#93a1a1
                       :background-halftone :#002b36
                       :foreground-halftone :#eee8d5
                       :link {:fresh :#cb4b16
                              :visited :#b58900
                              :hover :#dc322f }}
           :default {:background :#fff
                     :foreground :#333
                     :background-halftone :#efefef
                     :foreground-halftone :#888
                     :link {:fresh :#097
                            :visited :#054
                            :hover :#0a8 }}} keys))

(defn global-css 
  "Generates the entire CSS rules of the app"
  [params]
  (let [theme (params :theme)
        theme (if theme (keyword theme) :default)
        header-fonts (gen-fontlist (params :header-font) "Noticia Text" "PT Serif" "Georgia")
        text-fonts (gen-fontlist (params :text-font) "Georgia")
        background (color theme :background)
        foreground (color theme :foreground)
        background-halftone (color theme :background-halftone)
        foreground-halftone (color theme :foreground-halftone)
        link-fresh (color theme :link :fresh)
        link-visited (color theme :link :visited)
        link-hover (color theme :link :hover)]
    (css 
      (rule "a"
            :color link-fresh
            :text-decoration :none
            :border-bottom [:1px :dotted]
            (rule "&:hover"
                  :color link-hover)
            (rule "&:visited"
                  :color link-visited))
      (rule "#draft"
            :margin-bottom :3em)
      (rule ".ui-border"
            (thin-border foreground))
      (rule ".button"
            :cursor :pointer)
      (rule ".ui-elem"
            helvetica-neue
            (thin-border foreground)
            :padding :0.3em
            :opacity 0.8
            :font-size :1em
            :background background)
      (rule ".landing-button"
            :box-shadow [0 :2px :5px :#aaa]
            :text-decoration :none
            :font-size :1.5em
            :background :#0a2
            :border :none
            :border-radius :10px
            :padding :10px
            helvetica-neue
            (rule "&:hover"
                  :background :#0b2))
      (rule "#panel"
            helvetica-neue
            :position :fixed
            :width (% 100)
            :border-top [:1px :solid foreground-halftone]
            :background background-halftone
            :padding :0.2em
            :bottom :0px
            :font-size :0.7em
            :text-align :center
            (rule "a"
                  :border :none))
      (rule "html, body"
            :background background
            :color foreground
            :margin 0
            :padding 0)
      (rule "#stats"
            (rule "tr"
                  (rule "& > td:first-child"
                        :text-align :right)))
      (rule "table,tr,td"
            :margin 0
            :border :none)
      (rule "td"
            :padding :0.5em)
      (rule ".one-third-column"
            :line-height (% 120)
            :text-align :justify
            :vertical-align :top
            ; Replace this by arithmetic with css-lengths as soon as they fix the bug
            :width (px (quot page-width 3)))
      (rule ".helvetica-neue"
            helvetica-neue)
      (rule "#hero"
            :padding-top :5em
            :padding-bottom :5em
            :text-align :center
            (rule "h1"
                  :font-size :2.5em)
            (rule "h2"
                  helvetica-neue
                  :margin :2em))
      (rule "article"
            central-element
            :margin-top :5em
            :font-family text-fonts
            :text-align :justify
            :font-size :1.2em
            (rule "p"
                  :line-height (% 140))
            (rule "& > h1:first-child"
                  :text-align :center
                  :margin :2em))
      (rule ".centered"
            :text-align :center)
      (rule ".bottom-space"
            :margin-bottom :7em)
      (rule "pre"
            :border-radius :3px
            :padding :0.5em
            :border [:1px :dotted foreground-halftone]
            :background background-halftone)
      (rule "*:focus"
            :outline [:0px :none :transparent])
      (rule "textarea"
            :width (px page-width)
            :border-radius :5px
            :font-family :Courier
            :font-size :1em
            :border :none
            :height :500px)
      (rule ".hidden"
            :display :none)
      (rule ".central-element"
            central-element)
      (rule "fieldset"
            :border :none)
      (rule "h1"
            :font-size :2em)
      (rule "#dashed-line"
            :border-bottom [:1px :dashed foreground-halftone]
            :margin-top :3em
            :margin-bottom :3em)
      (rule "h1, h2, h3, h4" 
            :font-family header-fonts))))
