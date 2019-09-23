using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Massage.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Massage.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly MojContext _context;

        public LoginController(MojContext context)
        {
            _context = context;
        }

        [HttpGet]
        public ActionResult<IEnumerable<Korisnik>> GetKorisnici()
        {
            return _context.korisnici.ToList();
        }
        [HttpPost("{id}")]
        public async Task<ActionResult<Korisnik>> GetBrojTelefona(int id,Korisnik korisnik)
        {
            if (id != 0)
            {
                _context.Entry(korisnik).State = EntityState.Modified;
                await _context.SaveChangesAsync();

                return korisnik;
            }
            if (korisnik.Ime != null)
            {
                _context.korisnici.Add(korisnik);
                await _context.SaveChangesAsync();
                return korisnik;
            }
            else
            {
                Korisnik user = _context.korisnici.Where(i => i.BrojTelefona == korisnik.BrojTelefona && i.Lozinka == korisnik.Lozinka).FirstOrDefault();
                if (user != null)
                {
                    return user;

                }
                else
                {
                    return Unauthorized();
                }
            }
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<Korisnik>> GetKorisnik(long id)
        {
            var korisnik = await _context.korisnici.FindAsync(id);
            if(korisnik == null)
            {
                return NotFound();
            }
            return korisnik;
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<Korisnik>> EditKorisnik(long id,Korisnik novi)
        {
            if(id!= novi.Id)
            {
                return NotFound();
            }
            _context.Entry(novi).State = EntityState.Modified;
            await _context.SaveChangesAsync();
            return novi;
        } 

        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteKorisnik(long id)
        {
            var korisnik = await _context.korisnici.FindAsync(id);
            if (korisnik == null)
            {
                return NotFound();
            }
            _context.korisnici.Remove(korisnik);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }
}