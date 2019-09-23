using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Massage.Models;
using Massage.ViewModels;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Massage.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RezervacijeController : ControllerBase
    {
        private readonly MojContext _context;
        private int sati = DateTime.UtcNow.Hour + 2; //time zone


        public RezervacijeController(MojContext context)
        {
            _context = context;
        }



        [HttpGet("{id}")]
        public ActionResult<IEnumerable<RezervacijeVM>> GetRezervacije(int id)
        {
            if (sati == 24)
                sati = 0;
            var korisnikoveRezervacije = _context.rezervacije.Where(x => x.UserId == id).Include(x => x.Termin).ToList();
            List<Rezervacije> rezervacije = new List<Rezervacije>();
            foreach(var x in korisnikoveRezervacije)
            {
                string datum=null;
                if (x.Dan < 10 && x.Mjesec < 10)
                {
                    datum = x.Godina.ToString() + "-" + "0" + x.Mjesec.ToString() + "-" + "0" + x.Dan.ToString();

                }
                else if (x.Mjesec < 10)
                {
                    datum = x.Godina.ToString() + "-" + "0" + x.Mjesec.ToString() + "-" + x.Dan.ToString();

                }
                else if (x.Dan < 10)
                {
                    datum = x.Godina.ToString() + "-" + x.Mjesec.ToString() + "-" + "0" + x.Dan.ToString();

                }
                else
                {
                    datum = x.Godina.ToString() + "-" + x.Mjesec.ToString() + "-" + x.Dan.ToString();

                }
                DateTime datumRezervacije = DateTime.ParseExact(datum, "yyyy-MM-dd", CultureInfo.InvariantCulture);
                if (datumRezervacije > DateTime.Today)
                    rezervacije.Add(x);
                if (datumRezervacije == DateTime.Today && x.Termin.Vrijeme.Hour > sati)
                    rezervacije.Add(x);
            }

            List<RezervacijeVM> lista = new List<RezervacijeVM>();

            foreach(var s in rezervacije)
            {
                RezervacijeVM temp = new RezervacijeVM
                {
                    Id = s.Id,
                    Godina = s.Godina,
                    Mjesec = s.Mjesec,
                    Dan = s.Dan,
                    Termin = s.Termin.Termin,
                    TerminId = s.TerminId
                };
                lista.Add(temp);
            }
            if (lista.Count == 0)
            {
                return null;
            }
            return lista;
        }

        [HttpGet("{dan}/{mjesec}/{godina}")]
        public ActionResult<IEnumerable<Termini>> GetTermini(int dan, int mjesec, int godina)
        {
            if (sati == 24)
                sati = 0;
            List<Termini> Slobodni = _context.termini.ToList();
            List<Rezervacije> rezervacije = _context.rezervacije.Where(x => x.Dan == dan && x.Mjesec == mjesec && x.Godina == godina).ToList();

            List<Termini> SlobodniRez = new List<Termini>();
            if (dan == DateTime.Today.Day && mjesec == DateTime.Today.Month && godina == DateTime.Today.Year)
            {
                SlobodniRez = Slobodni.Where(x => x.Vrijeme.Hour > sati).ToList();
            }
            else
            { SlobodniRez = Slobodni; }
            foreach (var item in rezervacije)
            {
                var rez = SlobodniRez.Where(x => x.Id == item.TerminId).FirstOrDefault();
                if (rez != null)
                    SlobodniRez.Remove(rez);
            }

            if (SlobodniRez.Count == 0)
            {
                return null;
            }
            return SlobodniRez;
        }

        [HttpPost]
        public async Task<ActionResult<Rezervacije>> SpremiRezervaciju(Rezervacije rezervacija)
        {
            var found = _context.rezervacije.Include(x=>x.Termin).Where(x => x.UserId == rezervacija.UserId &&
            rezervacija.Dan == x.Dan && rezervacija.Mjesec == x.Mjesec &&
            rezervacija.Godina == x.Godina).FirstOrDefault();

            if (found != null)
            {
                return null;
            }
            else
            {
                var termin = _context.termini.Find(rezervacija.TerminId);
                rezervacija.Termin = termin;
                rezervacija.DatumRezervacije = DateTime.Now;
                _context.rezervacije.Add(rezervacija);
                await _context.SaveChangesAsync();
                return rezervacija;
            }
        }
        [HttpDelete("{id}")]
        public StatusCodeResult Delete(int id)
        {
            var rezv = _context.rezervacije.Find(id);
            _context.rezervacije.Remove(rezv);
            _context.SaveChanges();

            return new OkResult();
        }
    }
}
